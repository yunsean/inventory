package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import com.yoga.inventory.barcode.enums.BarcodeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class SaveSettingDto extends BaseDto {

	@Enumerated(EnumType.STRING)
	private BarcodeType type;
	private String read;
	private String clean;
	private String count;
	private String normal;
	private String stock;
}
