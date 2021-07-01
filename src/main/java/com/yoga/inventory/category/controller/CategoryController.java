package com.yoga.inventory.category.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseVo;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.inventory.category.dto.AddDto;
import com.yoga.inventory.category.dto.DeleteDto;
import com.yoga.inventory.category.dto.GetDto;
import com.yoga.inventory.category.dto.UpdateDto;
import com.yoga.inventory.category.model.Category;
import com.yoga.inventory.category.service.CategoryService;
import com.yoga.inventory.category.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Api(tags = "分类管理")
@Controller("inventoryCategoryController")
@RequestMapping("/admin/inventory/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @ApiIgnore
    @RequestMapping("")
    @RequiresPermissions("inventory_category")
    public String allColumn(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Category> categories = categoryService.all(dto.getTid());
        model.put("categories", categories);
        return "/admin/inventory/category/list";
    }

    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("分类列表")
    @RequestMapping("/list.json")
    public ApiResults<CategoryVo> list(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Category> categories = categoryService.all(dto.getTid());
        return new ApiResults<>(convertVo(categories));
    }
    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("分类详情")
    @RequestMapping("/get.json")
    public ApiResult<CategoryVo> subColumnsOfId(@Valid @ModelAttribute GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Category category = categoryService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(category, CategoryVo.class);
    }
    @ResponseBody
    @ApiOperation("添加分类")
    @RequestMapping("/add.json")
    @RequiresPermissions("inventory_category.edit")
    public ApiResult<Long> add(@Valid @ModelAttribute AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        long id = categoryService.add(dto.getTid(), dto.getName(), dto.getRemark(), dto.getParentId(), dto.getImage());
        return new ApiResult<>(id);
    }
    @ResponseBody
    @ApiOperation("删除分类")
    @RequestMapping("/delete.json")
    @RequiresPermissions("inventory_category.edit")
    public ApiResult delColumn(@Valid @ModelAttribute DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        categoryService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("修改分类")
    @RequiresPermissions("inventory_category.edit")
    @RequestMapping("/update.json")
    public ApiResult modifyColumn(@Valid @ModelAttribute UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        categoryService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getRemark(), dto.getParentId(), dto.getImage());
        return new ApiResult();
    }

    private List<CategoryVo> convertVo(Collection<Category> categories) {
        return BaseVo.copys(categories, CategoryVo.class, (po, vo)-> {
           vo.setChildren(convertVo(po.getChildren()));
        });
    }
}
