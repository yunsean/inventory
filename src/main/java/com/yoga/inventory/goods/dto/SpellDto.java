package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpellDto extends BaseDto {

	@ApiModelProperty(value = "姓名")
	private String name;
}
