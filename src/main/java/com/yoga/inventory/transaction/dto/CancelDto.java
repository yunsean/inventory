package com.yoga.inventory.transaction.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class CancelDto extends BaseDto {

	@ApiModelProperty(value = "记录ID")
	@NotNull(message = "记录ID不能为空")
	private Long id;
}
