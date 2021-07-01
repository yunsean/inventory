package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class CheckCodeDto extends BaseDto {

	@ApiModelProperty(value = "资产条码")
	@NotBlank(message = "资产条码不能为空")
	private String barcode;
}
