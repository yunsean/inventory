package com.yoga.inventory.transaction.dto;

import com.yoga.core.base.BaseDto;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class ListDto extends BaseDto {

	@ApiModelProperty(value = "开始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate beginDate;
	@ApiModelProperty(value = "结束日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	@ApiModelProperty(value = "操作类型")
	@Enumerated(EnumType.STRING)
	private OperateEnum operate;
	@ApiModelProperty(value = "操作状态")
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	@ApiModelProperty(value = "操作用户ID")
	private Long userId;
	@ApiModelProperty(value = "操作用户")
	private String user;
	@ApiModelProperty(value = "资产编码")
	private Long goodsId;
	@ApiModelProperty(value = "资产条码")
	private String barcode;
}
