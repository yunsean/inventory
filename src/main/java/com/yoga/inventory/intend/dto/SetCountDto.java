package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class SetCountDto extends BaseDto {

	@ApiModelProperty(value = "资产ID")
	@NotNull(message = "资产ID不能为空")
	private Long id;
	@ApiModelProperty(value = "设置数量")
	@NotNull(message = "数量不能为空")
	@Min(value = 1, message = "数量不能小于1")
	private Integer count;
}
