package com.yoga.inventory.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseVo;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.PinyinUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.excelkit.ExcelKit;
import com.yoga.inventory.barcode.dto.QueryDto;
import com.yoga.inventory.barcode.model.Barcode;
import com.yoga.inventory.barcode.service.BarcodeService;
import com.yoga.inventory.category.model.Category;
import com.yoga.inventory.category.service.CategoryService;
import com.yoga.inventory.goods.dto.*;
import com.yoga.inventory.goods.model.Goods;
import com.yoga.inventory.goods.service.GoodsService;
import com.yoga.inventory.goods.vo.BarcodeVo;
import com.yoga.inventory.goods.vo.BriefGoodsVo;
import com.yoga.inventory.goods.vo.GoodsVo;
import com.yoga.inventory.goods.vo.SpellVo;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.model.TransactionItem;
import com.yoga.inventory.transaction.service.TransactionService;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.customize.CustomPage;
import com.yoga.utility.feie.service.FeiePrintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Api(tags = "资产管理")
@Controller("inventoryGoodsController")
@RequestMapping("/admin/inventory/goods")
@Settable(module = GoodsService.ModuleName, key = GoodsService.Key_AutoGenCode, name = "资产管理-自动生成资产编码格式（如CODE000000)")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BarcodeService barcodeService;
    @Autowired
    private FeiePrintService printService;
    @Autowired
    private TransactionService transactionService;

    @ApiIgnore
    @RequestMapping("")
    @RequiresPermissions("inventory_goods")
    public String goodsList(ModelMap model, CustomPage page, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getCategoryId() != null && dto.getCategoryId() == 0) dto.setCategoryId(null);
        Boolean preferApply = dto.isPreferApply() ? true : null;
        Boolean understock = dto.isUnderstock() ? true : null;
        PageInfo<Goods> goods = goodsService.list(dto.getTid(), dto.getCategoryId(), dto.getBarcode(), null, dto.getKeyword(), understock, preferApply, page.getPageIndex(), page.getPageSize());
        List<Category> categories = categoryService.all(dto.getTid());
        Map<String, Object> params = dto.wrapAsMap();
        model.put("param", params);
        model.put("categories", categories);
        model.put("goods", goods.getList());
        model.put("page", new CommonPage(goods));
        return "/admin/inventory/goods/list";
    }
    @ApiIgnore
    @RequestMapping("/detail")
    @RequiresPermissions("inventory_goods")
    public String goodsDetail(ModelMap model, CommonPage page, @Valid DetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Goods goods = goodsService.get(dto.getTid(), dto.getId());
        boolean detailIsJson = true;
        try {
            JSONObject jsonObject = JSONObject.parseObject(goods.getDetail());
            detailIsJson = jsonObject != null;
        } catch (Exception ex) {
            detailIsJson = false;
        }
        PageInfo<TransactionItem> items = transactionService.detail(dto.getTid(), null, null, dto.getId(), dto.getOperate(), dto.getBeginDate(), dto.getEndDate(), null, null, page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("goods", goods);
        model.put("items", items.getList());
        model.put("page", new CommonPage(items));
        model.put("detailIsJson", detailIsJson);
        model.put("operates", OperateEnum.values());
        return "/admin/inventory/goods/detail";
    }
    @ApiIgnore
    @ResponseBody
    @RequestMapping("/detail/export")
    @RequiresPermissions("inventory_goods")
    public void exportExcel(HttpServletResponse response, @Valid DetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<TransactionItem> items = transactionService.detail(dto.getTid(), null, null, dto.getId(), dto.getOperate(), dto.getBeginDate(), dto.getEndDate(), null, null);
        ExcelKit.$Export(TransactionItem.class, response).downXlsx(items, false);
    }
    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("inventory_goods")
    @RequestMapping("/export")
    public void exportExcel(HttpServletResponse response, @Valid ExportDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getCategoryId() != null && dto.getCategoryId() == 0) dto.setCategoryId(null);
        Boolean understock = dto.isUnderstock() ? true : null;
        Boolean preferApply = dto.isPreferApply() ? true : null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        List<Goods> goodses = goodsService.list(dto.getTid(), dto.getCategoryId(), dto.getBarcode(), null, dto.getKeyword(), understock, preferApply);
        ExcelKit.$Export(Goods.class, response).downXlsx(goodses, false);
    }


    @ResponseBody
    @GetMapping("/spell.json")
    @RequiresAuthentication
    @ApiOperation(value = "获取资产拼音")
    public ApiResult<SpellVo> get(@Valid @ModelAttribute SpellDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        SpellVo vo = new SpellVo();
        vo.setName(dto.getName());
        vo.setSpell(PinyinUtil.toPinyin(dto.getName()));
        vo.setInitial(PinyinUtil.setInitial(dto.getName()));
        return new ApiResult<>(vo);
    }
    @ResponseBody
    @ApiOperation("添加资产")
    @PostMapping("/add.json")
    @RequiresPermissions("inventory_goods.edit")
    public ApiResult<Long> addGoods(@Valid @ModelAttribute AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StringUtil.isNotBlank(dto.getBarcode()) && dto.getBarcode().length() != 13) throw new BusinessException("请输入13位长度的正确条码!");
        long goodsId = goodsService.add(dto.getTid(), dto.getCategoryId(), dto.getBarcode(), dto.getCode(), dto.getName(), dto.getSpell(), dto.getInitial(), dto.getTradeMark(), dto.getManuName(), dto.getSpec(), dto.getImage(), dto.getDetail(), dto.getUnits(), dto.getShelf(), dto.getPreferred(), dto.getPreferred(), dto.getRemain());
        return new ApiResult<>(goodsId);
    }

    @ResponseBody
    @ApiOperation("删除资产")
    @DeleteMapping("/delete.json")
    @RequiresPermissions("inventory_goods.edit")
    public ApiResult delGoods(@Valid @ModelAttribute DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        goodsService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }

    @ResponseBody
    @ApiOperation("修改资产")
    @PostMapping("/update.json")
    @RequiresPermissions("inventory_goods.edit")
    public ApiResult updateGoods(@Valid @ModelAttribute UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        goodsService.update(dto.getTid(), dto.getId(), dto.getCategoryId(), dto.getBarcode(), dto.getCode(), dto.getName(), dto.getSpell(), dto.getInitial(), dto.getTradeMark(), dto.getManuName(), dto.getSpec(), dto.getImage(), dto.getDetail(), dto.getUnits(), dto.getShelf(), dto.getThreshold(), dto.getPreferred(), dto.getRemain(), dto.getConsumed(), dto.getInputted());
        return new ApiResult();
    }

    @ResponseBody
    @ApiOperation("资产列表")
    @GetMapping("/list.json")
    @RequiresAuthentication
    public ApiResults<GoodsVo> listGoods(@ModelAttribute CustomPage page, @Valid @ModelAttribute ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Goods> goods = goodsService.list(dto.getTid(), dto.getCategoryId(), dto.getBarcode(), null, dto.getKeyword(), dto.getUnderstock(), dto.getPreferAdd(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(goods, GoodsVo.class, (po, vo)-> vo.setDetail(null));
    }
    @ResponseBody
    @ApiOperation("资产列表")
    @GetMapping("/filter.json")
    @RequiresAuthentication
    public ApiResults<BriefGoodsVo> filterGoods(@ModelAttribute CustomPage page, @Valid @ModelAttribute ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Goods> goods = goodsService.list(dto.getTid(), dto.getCategoryId(), dto.getBarcode(), null, dto.getKeyword(), dto.getUnderstock(), dto.getPreferAdd(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(goods, BriefGoodsVo.class);
    }

    @ResponseBody
    @ApiOperation("资产详情")
    @GetMapping("/get.json")
    @RequiresAuthentication
    public ApiResult<GoodsVo> getGoods(@Valid @ModelAttribute GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Goods goods = goodsService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(goods, GoodsVo.class, (po, vo)-> {
            try {
                vo.setDetail(JSONObject.parse(po.getDetail()));
            } catch (Throwable ex) {
                vo.setDetail(po.getDetail());
            }
        });
    }

    @ResponseBody
    @ApiOperation("资产查询")
    @GetMapping("/query.json")
    @RequiresAuthentication
    public ApiResult<BarcodeVo> queryGoods(@Valid @ModelAttribute QueryDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Goods goods = goodsService.get(dto.getTid(), dto.getBarcode(), true);
        if (goods != null) {
            return new ApiResult<>(new BarcodeVo(BaseVo.copy(goods, GoodsVo.class, (po, vo)-> {
                try {
                    vo.setDetail(JSONObject.parse(po.getDetail()));
                } catch (Throwable ex) {
                    vo.setDetail(po.getDetail());
                }
            })));
        } else {
            try {
                Barcode barcode = barcodeService.getBarcode(dto.getBarcode());
                try {
                    return new ApiResult<>(new BarcodeVo(JSONObject.parse(barcode.getDetail())));
                } catch (Throwable ex) {
                    return new ApiResult<>(new BarcodeVo(barcode.getDetail()));
                }
            } catch (Exception ex) {
                return new ApiResult<>(new BarcodeVo());
            }
        }
    }

    @ResponseBody
    @ApiOperation("打印库存不足资产")
    @RequiresAuthentication
    @PostMapping("/print/stockout.json")
    public ApiResult printStockout(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Goods> goodses = goodsService.list(dto.getTid(), null, null, null, null, true, false);
        StringBuilder content = new StringBuilder("<CB>库存不足</CB>");
        for (Goods goods : goodses) {
            content.append(goods.getName());
            content.append("<RIGHT><BOLD> ");
            content.append(goods.getRemain());
            content.append(" </BOLD></RIGHT><BR>");
        }
        printService.print(dto.getTid(), content.toString());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("打印条码")
    @RequiresPermissions("inventory_goods.print")
    @PostMapping("/print.json")
    public ApiResult printBarcode(@Valid PrintDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Goods goods = goodsService.get(dto.getTid(), dto.getId());
        String code = goods.getBarcode();
        String content;
        content = "<C>" + printService.getBarCode(code) + "</C>";
        content += "<C>" + goods.getName() + "</C>";
        for (int i = 0; i < dto.getCount(); i++) {
            printService.print(dto.getTid(), content);
        }
        return new ApiResult();
    }
}
