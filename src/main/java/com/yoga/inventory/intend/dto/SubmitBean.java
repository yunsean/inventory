package com.yoga.inventory.intend.dto;

import com.yoga.inventory.intend.ao.ApplyItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SubmitBean {

	@ApiModelProperty(value = "备注信息")
	@Size(max = 2000, message = "备注信息不能超过2000字符")
	private String remark;

	@ApiModelProperty(value = "申请资产列表")
	private List<ApplyItem> items;
}
