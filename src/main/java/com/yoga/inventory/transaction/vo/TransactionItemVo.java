package com.yoga.inventory.transaction.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionItemVo {


    @ApiModelProperty(value = "记录ID")
    private Long transactionId;
    @ApiModelProperty(value = "操作状态")
    private TransactionStatus status;
    @ApiModelProperty(value = "状态名称")
    private String statusName;
    @ApiModelProperty(value = "操作类型")
    private OperateEnum operate;
    @ApiModelProperty(value = "类型名称")
    private String operateName;
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    @ApiModelProperty(value = "资产ID")
    private Long goodsId;
    @ApiModelProperty(value = "资产名称")
    private String name;
    @ApiModelProperty(value = "资产编码")
    private String code;
    @ApiModelProperty(value = "资产条码")
    private String barcode;
    @ApiModelProperty(value = "资产品牌")
    private String tradeMark;
    @ApiModelProperty(value = "生产厂家")
    private String manuName;
    @ApiModelProperty(value = "规格参数")
    private String spec;
    @ApiModelProperty(value = "资产图标")
    private String image;

    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户姓名")
    private String nickname;
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "所属品类ID")
    private Long categoryId;
    @ApiModelProperty(value = "所属品类")
    private String category;

    @ApiModelProperty(value = "操作数量")
    private Integer count;
}

