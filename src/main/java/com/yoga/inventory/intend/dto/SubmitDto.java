package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class SubmitDto extends BaseDto {

	@ApiModelProperty(value = "备注信息")
	@Size(max = 2000, message = "备注信息不能超过2000字符")
	private String remark;
}
