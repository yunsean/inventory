package com.yoga.inventory.category.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryVo {

    @ApiModelProperty(value = "分类ID")
    private Long id;
    @ApiModelProperty(value = "分类名称")
    private String name;
    @ApiModelProperty(value = "备注信息")
    private String remark;
    @ApiModelProperty(value = "父级分类ID")
    private Long parentId;
    @ApiModelProperty(value = "分类图标")
    private String image;
    @ApiModelProperty(value = "子分类")
    private List<CategoryVo> children;
}

