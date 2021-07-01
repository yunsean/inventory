package com.yoga.inventory.goods.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpellVo {

	@ApiModelProperty(value = "姓名")
	private String name;
	@ApiModelProperty(value = "全拼")
	private String spell;
	@ApiModelProperty(value = "首字母")
	private String initial;
}
