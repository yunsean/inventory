package com.yoga.inventory.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsVo {

    @ApiModelProperty(value = "资产ID")
    private Long id;
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "所属分类")
    private String category;
    @ApiModelProperty(value = "资产条码")
    private String barcode;
    @ApiModelProperty(value = "资产编码")
    private String code;
    @ApiModelProperty(value = "资产名称")
    private String name;
    @ApiModelProperty(value = "名称全拼")
    private String spell;
    @ApiModelProperty(value = "名称首字母")
    private String initial;
    @ApiModelProperty(value = "商品品牌")
    private String tradeMark;
    @ApiModelProperty(value = "生产厂家")
    private String manuName;
    @ApiModelProperty(value = "规格参数")
    private String spec;
    @ApiModelProperty(value = "资产图标")
    private String image;
    @ApiModelProperty(value = "详细信息")
    private Object detail;
    @ApiModelProperty(value = "计量单位")
    private String units;
    @ApiModelProperty(value = "货架号")
    private String shelf;

    @ApiModelProperty(value = "添加日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;
    @ApiModelProperty(value = "最后修改")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "库存阈值")
    private Integer threshold;
    @ApiModelProperty(value = "采购阈值")
    private Integer preferred;
    @ApiModelProperty(value = "当前库存")
    private Integer remain;
    @ApiModelProperty(value = "总消耗量")
    private Integer consumed;
    @ApiModelProperty(value = "总入库量")
    private Integer inputted;
}

