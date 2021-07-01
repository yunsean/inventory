package com.yoga.inventory.intend.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.DateUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.inventory.category.model.Category;
import com.yoga.inventory.category.service.CategoryService;
import com.yoga.inventory.goods.model.Goods;
import com.yoga.inventory.goods.service.GoodsService;
import com.yoga.inventory.intend.dto.*;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.intend.model.Intend;
import com.yoga.inventory.intend.service.IntendService;
import com.yoga.inventory.intend.vo.IntendVo;
import com.yoga.inventory.transaction.service.TransactionService;
import com.yoga.operator.user.model.User;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "入库管理")
@Controller("inventoryInputController")
@RequestMapping("/admin/inventory/input")
public class InputController extends BaseController {

    @Autowired
    private IntendService intendService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TransactionService transactionService;

    @ApiIgnore
    @RequestMapping("")
    @RequiresPermissions("inventory_input")
    public String listInput(ModelMap model, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        List<Category> categories = categoryService.all(dto.getTid());
        model.put("param", dto.wrapAsMap());
        model.put("categories", categories);
        return "/admin/inventory/input/input";
    }
    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("/list")
    public String listInputIntend(ModelMap model, CustomPage page, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        PageInfo<Intend> records = intendService.list(dto.getTid(), user.getId(), dto.getBarcode(), dto.getCategoryId(), null, dto.getKeyword(), OperateEnum.INPUT, page.getPageIndex(), page.getPageSize());
        List<Category> categories = categoryService.all(dto.getTid());
        model.put("param", dto.wrapAsMap());
        model.put("records", records.getList());
        model.put("catalogs", categories);
        model.put("page", new CommonPage(records));
        return "/admin/inventory/input/list";
    }

    @ResponseBody
    @ApiOperation("待入库列表")
    @RequiresPermissions("inventory_input.input")
    @GetMapping("/list.json")
    public ApiResults<IntendVo> listInput(CommonPage page, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        PageInfo<Intend> records = intendService.list(dto.getTid(), user.getId(), dto.getBarcode(), dto.getCategoryId(), null, dto.getKeyword(), OperateEnum.INPUT, page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(records, IntendVo.class, (po, vo)-> vo.setOperateName(po.getOperate().getName()));
    }

    @ResponseBody
    @ApiOperation("添加待入库条码")
    @RequiresPermissions("inventory_input.input")
    @PostMapping("/add/code.json")
    public ApiResults<IntendVo> addInput(@Valid @ModelAttribute AddByCodeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StringUtil.isBlank(dto.getBarcode())) throw new BusinessException("请输入资产条码！");
        User user = User.getLoginUser();
        if (dto.isImmediate()) {
            if (StringUtil.isBlank(dto.getRemark())) dto.setRemark(user.getNickname() + "于" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm") + "入库资产！");
            Goods goods = goodsService.get(dto.getTid(), dto.getBarcode(), false);
            transactionService.add(dto.getTid(), user.getId(), OperateEnum.INPUT, goods.getId(), dto.getCount(), dto.getRemark());
        } else {
            intendService.add(dto.getTid(), user.getId(), dto.getBarcode(), OperateEnum.INPUT, dto.getCount());
        }
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }
    @ResponseBody
    @ApiOperation("添加待入库批号")
    @RequiresPermissions("inventory_input.input")
    @PostMapping("/add/id.json")
    public ApiResults<IntendVo> addInputById(@Valid @ModelAttribute AddByIdDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        if (dto.isImmediate()) {
            if (StringUtil.isBlank(dto.getRemark())) dto.setRemark(user.getNickname() + "于" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm") + "入库资产！");
            transactionService.add(dto.getTid(), user.getId(), OperateEnum.INPUT, dto.getId(), dto.getCount(), dto.getRemark());
        } else {
            intendService.add(dto.getTid(), user.getId(), dto.getId(), OperateEnum.INPUT, dto.getCount());
        }
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }
    @ResponseBody
    @ApiOperation("批量添加待入库条码")
    @RequiresPermissions("inventory_input.input")
    @PostMapping("/add/batch.json")
    public ApiResults<IntendVo> batchAddInput(@Valid @ModelAttribute BatchAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.batchAdd(dto.getTid(), user.getId(), dto.readBarcodes(), OperateEnum.INPUT);
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }

    @ResponseBody
    @ApiOperation("修改待入库数量")
    @RequiresPermissions("inventory_input.input")
    @PostMapping("/add/set.json")
    public ApiResults<IntendVo> setInput(@Valid @ModelAttribute SetCountDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.setCount(dto.getTid(), user.getId(), dto.getId(), OperateEnum.INPUT, dto.getCount());
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }


    @ResponseBody
    @ApiOperation("删除待入库资产")
    @RequiresPermissions("inventory_input.input")
    @DeleteMapping("/delete.json")
    public ApiResults<IntendVo> delInput(@Valid @ModelAttribute DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.delete(dto.getTid(), user.getId(), dto.getId(), OperateEnum.INPUT);
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }
    @ResponseBody
    @ApiOperation("清空待入库列表")
    @RequiresPermissions("inventory_input.input")
    @DeleteMapping("/clean.json")
    public ApiResults<IntendVo> cleanInput(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.clear(dto.getTid(), user.getId(), OperateEnum.INPUT);
        return new ApiResults<>();
    }

    @ResponseBody
    @ApiOperation("提交待入库")
    @RequiresPermissions("inventory_input.input")
    @PostMapping("/submit.json")
    public ApiResult<Long> submitInput(@Valid @ModelAttribute SubmitDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        if (StringUtil.isBlank(dto.getRemark())) dto.setRemark(user.getNickname() + "于" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm") + "入库资产！");
        long id = intendService.commit(dto.getTid(), user.getId(), OperateEnum.INPUT, null, dto.getRemark());
        return new ApiResult<>(id);
    }
}
