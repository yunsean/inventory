package com.yoga.inventory.category.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class DeleteDto extends BaseDto {

	@ApiModelProperty(value = "分类ID")
	@NotNull(message = "分类ID不能为空")
	private Long id;
}
