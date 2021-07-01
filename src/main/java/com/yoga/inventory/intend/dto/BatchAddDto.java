package com.yoga.inventory.intend.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class BatchAddDto extends BaseDto {

	@ApiModelProperty(value = "入库条码列表，以回车分割")
	@NotEmpty(message = "条码不能为空")
	private String barcodes;

	public List<String> readBarcodes() {
		return Arrays.stream(barcodes.split("\n")).collect(Collectors.toList());
	}
}
