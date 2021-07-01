package com.yoga.inventory.barcode.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class QueryDto extends BaseDto {

	@ApiModelProperty(value = "条码")
	@NotBlank(message = "条码不能为空")
	private String barcode;
}
