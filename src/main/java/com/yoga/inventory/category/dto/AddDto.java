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
public class AddDto extends BaseDto {

	@ApiModelProperty(value = "分类名称")
	@NotNull(message = "分类名称不能为空")
	private String name;
	@ApiModelProperty(value = "备注信息")
	private String remark;
	@ApiModelProperty(value = "分类图标")
	private String image;
	@ApiModelProperty(value = "父级分类ID，默认根分类")
	private long parentId = 0;
}
