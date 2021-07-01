package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FilterDto extends BaseDto {

	@ApiModelProperty(value = "资产条码，可选")
	private String barcode;
	@ApiModelProperty(value = "资产分类ID，可选")
	private Long categoryId;
	@ApiModelProperty(value = "资产关键字，可选")
	private String keyword;

	public FilterDto(long tid) {
		setTid(tid);
	}
}
