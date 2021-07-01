package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class AddByCodeDto extends BaseDto {

	@ApiModelProperty(value = "资产条码")
	@NotBlank(message = "条码不能为空")
	@Size(max = 32, message = "条码不能超过32字符")
	private String barcode;
	@ApiModelProperty(value = "数量")
	@NotNull(message = "数量不能为空")
	@Min(value = 1, message = "添加数量不能小于1")
	private Integer count;
	@ApiModelProperty(value = "强制出库")
	private boolean forceOutput = false;
	@ApiModelProperty(value = "立即出库")
	private boolean immediate = false;
	@ApiModelProperty(value = "备注信息")
	private String remark;
}
