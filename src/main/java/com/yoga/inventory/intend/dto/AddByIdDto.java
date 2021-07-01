package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddByIdDto extends BaseDto {

	@ApiModelProperty(value = "资产ID")
	@NotNull(message = "资产ID不能为空")
	private Long id;
	@ApiModelProperty(value = "数量")
	@NotNull(message = "数量不能为空")
	private Integer count;
	@ApiModelProperty(value = "强制出库")
	private boolean forceOutput = false;
	@ApiModelProperty(value = "立即出库")
	private boolean immediate = false;
	@ApiModelProperty(value = "备注信息")
	private String remark;
}
