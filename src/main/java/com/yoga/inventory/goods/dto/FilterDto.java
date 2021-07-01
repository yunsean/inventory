package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FilterDto extends BaseDto {

	private String keyword;
	private Long categoryId;
	private String barcode;
	private boolean understock = false;
	private boolean preferApply = false;
}
