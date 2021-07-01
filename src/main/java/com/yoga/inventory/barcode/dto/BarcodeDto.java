package com.yoga.inventory.barcode.dto;

import com.yoga.core.base.BaseDto;
import com.yoga.inventory.barcode.enums.BarcodeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class BarcodeDto extends BaseDto {

	@ApiModelProperty(value = "条码内容")
	private String barcode = "1234567890";
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(value = "条码类型")
	private BarcodeType type = BarcodeType.CODE39;
	@ApiModelProperty(value = "条码高度，缺省值15mm")
	private String height = "15mm";
	@ApiModelProperty(value = "线条宽度，缺省值0.25mm")
	private String moduleWidth = "0.3mm";
	private String placement;
	private String fontSize;
}
