package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ListDto extends BaseDto {

	@ApiModelProperty(value = "关键字")
	private String keyword;
	@ApiModelProperty(value = "分类ID")
	private Long categoryId;
	@ApiModelProperty(value = "条码")
	private String barcode;
	@ApiModelProperty(value = "库存不足")
	private Boolean understock;
	@ApiModelProperty(value = "建议补充")
	private Boolean preferAdd;
}
