package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class QueryDto extends BaseDto {

	@ApiModelProperty(value = "资产条码")
	@NotNull(message = "条码不能为空")
	private String barcode;
}
