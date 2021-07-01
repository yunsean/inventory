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
public class UpdateDto extends BaseDto {

	@ApiModelProperty(value = "分类ID")
	@NotNull(message = "分类ID不能为空")
	private Long id;
	@ApiModelProperty(value = "新分类名称")
	private String name;
	@ApiModelProperty(value = "新备注信息")
	private String remark;
	@ApiModelProperty(value = "新分类图标")
	private String image;
	@ApiModelProperty(value = "新父级分类ID")
	private Long parentId;
}
