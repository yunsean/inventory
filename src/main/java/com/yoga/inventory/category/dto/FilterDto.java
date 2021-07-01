package com.yoga.inventory.category.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FilterDto extends BaseDto {

	@ApiModelProperty(value = "过滤条件")
	private String filter;
}
