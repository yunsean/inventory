package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class RefreshDto extends BaseDto {

	@NotBlank(message = "条码不能为空")
	private String barcode;
}
