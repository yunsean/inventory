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
public class TransactionVo {

    @ApiModelProperty(value = "记录ID")
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户名称")
    private String nickname;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "记录状态")
    private TransactionStatus status;
    @ApiModelProperty(value = "状态名称")
    private String statusName;
    @ApiModelProperty(value = "操作类型")
    private OperateEnum operate;
    @ApiModelProperty(value = "类型名称")
    private String operateName;
    @ApiModelProperty(value = "备注信息")
    private String remark;
    @ApiModelProperty(value = "资产总数")
    private Integer totalCount;
    @ApiModelProperty(value = "品类总数")
    private Integer kindCount;
    @ApiModelProperty(value = "记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;
}

