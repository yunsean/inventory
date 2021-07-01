package com.yoga.inventory.intend.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyItem {

	@ApiModelProperty(value = "资产ID")
	@NotNull(message = "资产ID不能为空")
	private Long id;
	@ApiModelProperty(value = "数量")
	@NotNull(message = "数量不能为空")
	private Integer count;
}
