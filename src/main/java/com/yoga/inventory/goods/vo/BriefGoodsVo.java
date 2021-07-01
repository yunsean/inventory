package com.yoga.inventory.goods.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BriefGoodsVo {

    @ApiModelProperty(value = "资产ID")
    private Long id;
    @ApiModelProperty(value = "所属分类")
    private String category;
    @ApiModelProperty(value = "资产条码")
    private String barcode;
    @ApiModelProperty(value = "资产名称")
    private String name;
    @ApiModelProperty(value = "规格参数")
    private String spec;
}

