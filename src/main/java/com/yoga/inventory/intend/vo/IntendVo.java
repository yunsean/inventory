package com.yoga.inventory.intend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.inventory.intend.enums.OperateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntendVo {

    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "资产ID")
    private Long goodsId;
    @ApiModelProperty(value = "资产条码")
    private String barcode;
    @ApiModelProperty(value = "资产编码")
    private String code;
    @ApiModelProperty(value = "资产名称")
    private String name;
    @ApiModelProperty(value = "商品品牌")
    private String tradeMark;
    @ApiModelProperty(value = "生产厂家")
    private String manuName;
    @ApiModelProperty(value = "规格参数")
    private String spec;
    @ApiModelProperty(value = "资产图标")
    private String image;
    @ApiModelProperty(value = "库存")
    private Integer remain;
    @ApiModelProperty(value = "计量单位")
    private String units;

    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "分类名称")
    private String category;

    @ApiModelProperty(value = "操作类型")
    private OperateEnum operate;
    @ApiModelProperty(value = "操作类型名称")
    private String operateName;
    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;
}

