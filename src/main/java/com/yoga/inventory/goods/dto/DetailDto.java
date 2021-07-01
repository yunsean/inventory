package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import com.yoga.inventory.intend.enums.OperateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class DetailDto extends BaseDto {

	@ApiModelProperty(value = "资产ID")
	@NotNull(message = "资产ID不能为空")
	private Long id;
	@ApiModelProperty(value = "开始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate beginDate;
	@ApiModelProperty(value = "结束日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	@ApiModelProperty(value = "操作类型")
	@Enumerated(EnumType.STRING)
	private OperateEnum operate;
}
