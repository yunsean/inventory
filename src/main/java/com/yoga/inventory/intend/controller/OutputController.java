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
import com.yoga.setting.annotation.Settable;
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

@Api(tags = "领用申请")
@Controller("inventoryOutputController")
@RequestMapping("/admin/inventory/output")
@Settable(module = IntendService.ModuleName, key = IntendService.Key_NoAffirm, name = "资产申请-申请流程无需确认", type = boolean.class, defaultValue = "false")
public class OutputController extends BaseController {

    @Autowired
    private IntendService intendService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;

    @ApiIgnore
    @RequestMapping("")
    @RequiresPermissions("inventory_output")
    public String listInput(ModelMap model, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        model.put("param", dto.wrapAsMap());
        model.put("categories", categoryService.all(dto.getTid()));
        return "/admin/inventory/output/output";
    }
    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("/list")
    public String listInputIntend(ModelMap model, CustomPage page, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        PageInfo<Intend> records = intendService.list(dto.getTid(), user.getId(), dto.getBarcode(), dto.getCategoryId(), null, dto.getKeyword(), OperateEnum.OUTPUT, page.getPageIndex(), page.getPageSize());
        List<Category> categories = categoryService.all(dto.getTid());
        model.put("param", dto.wrapAsMap());
        model.put("records", records.getList());
        model.put("catalogs", categories);
        model.put("page", new CommonPage(records));
        return "/admin/inventory/output/list";
    }

    @ResponseBody
    @ApiOperation("登录用户申请列表")
    @RequiresPermissions("inventory_output")
    @GetMapping("/list.json")
    public ApiResults<IntendVo> listInput(CommonPage page, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        PageInfo<Intend> records = intendService.list(dto.getTid(), user.getId(), dto.getBarcode(), dto.getCategoryId(), null, dto.getKeyword(), OperateEnum.OUTPUT, page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(records, IntendVo.class, (po, vo)-> vo.setOperateName(po.getOperate().getName()));
    }

    @ResponseBody
    @ApiOperation("按条码申请资产")
    @RequiresPermissions("inventory_output")
    @PostMapping("/add/code.json")
    public ApiResults<IntendVo> addInput(@Valid @ModelAttribute AddByCodeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StringUtil.isBlank(dto.getBarcode())) throw new BusinessException("请输入资产条码！");
        User user = User.getLoginUser();
        if (dto.isImmediate()) {
            if (StringUtil.isBlank(dto.getRemark())) dto.setRemark(user.getNickname() + "于" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm") + "申请领用资产！");
            Goods goods = goodsService.get(dto.getTid(), dto.getBarcode(), false);
            transactionService.add(dto.getTid(), user.getId(), OperateEnum.OUTPUT, goods.getId(), dto.getCount(), dto.getRemark());
        } else {
            intendService.add(dto.getTid(), user.getId(), dto.getBarcode(), OperateEnum.OUTPUT, dto.getCount());
        }
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }
    @ResponseBody
    @ApiOperation("按ID申请资产")
    @RequiresPermissions("inventory_output")
    @PostMapping("/add/id.json")
    public ApiResults<IntendVo> addInputById(@Valid @ModelAttribute AddByIdDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        if (dto.isImmediate()) {
            if (StringUtil.isBlank(dto.getRemark())) dto.setRemark(user.getNickname() + "于" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm") + "入库资产！");
            transactionService.add(dto.getTid(), user.getId(), OperateEnum.OUTPUT, dto.getId(), dto.getCount(), dto.getRemark());
        } else {
            intendService.add(dto.getTid(), user.getId(), dto.getId(), OperateEnum.OUTPUT, dto.getCount());
        }
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }
    @ResponseBody
    @ApiOperation("修改s很轻数量")
    @RequiresPermissions("inventory_output")
    @PostMapping("/add/set.json")
    public ApiResults<IntendVo> setInput(@Valid @ModelAttribute SetCountDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.setCount(dto.getTid(), user.getId(), dto.getId(), OperateEnum.OUTPUT, dto.getCount());
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }


    @ResponseBody
    @ApiOperation("删除资产申请")
    @RequiresPermissions("inventory_output")
    @DeleteMapping("/delete.json")
    public ApiResults<IntendVo> delInput(@Valid @ModelAttribute DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.delete(dto.getTid(), user.getId(), dto.getId(), OperateEnum.OUTPUT);
        return listInput(new CommonPage(), new FilterDto(dto.getTid()), bindingResult);
    }
    @ResponseBody
    @ApiOperation("清空申请列表")
    @RequiresPermissions("inventory_output")
    @DeleteMapping("/clean.json")
    public ApiResults<IntendVo> cleanInput(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        intendService.clear(dto.getTid(), user.getId(), OperateEnum.OUTPUT);
        return new ApiResults<>();
    }

    @ResponseBody
    @ApiOperation("提交申请")
    @RequiresPermissions("inventory_output")
    @PostMapping(value = "/submit.json")
    public ApiResult<Long> submitInput(@Valid @ModelAttribute BaseDto dto, @RequestBody @Valid SubmitBean bean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        if (StringUtil.isBlank(bean.getRemark())) bean.setRemark(user.getNickname() + "于" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm") + "申请领用资产！");
        long id = intendService.commit(dto.getTid(), user.getId(), OperateEnum.OUTPUT, bean.getItems(), bean.getRemark());
        return new ApiResult<>(id);
    }
}
