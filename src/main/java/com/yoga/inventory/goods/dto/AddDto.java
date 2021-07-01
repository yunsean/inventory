package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor
public class AddDto extends BaseDto {

	@ApiModelProperty(value = "所属分类")
	@NotNull(message = "所属分类不能为空")
	private Long categoryId;
	@ApiModelProperty(value = "资产条码，为空则自动生成")
	@Pattern(regexp = "^[0-9]*$", message = "请输入正确的条形码")
	private String barcode;
	@ApiModelProperty(value = "资产编码")
	private String code;
	@ApiModelProperty(value = "资产名称")
	@NotBlank(message = "请输入资产名称")
	private String name;
	@ApiModelProperty(value = "名称全拼")
	private String spell;
	@ApiModelProperty(value = "名称首字母")
	private String initial;
	@ApiModelProperty(value = "资产品牌")
	private String tradeMark;
	@ApiModelProperty(value = "生产厂家")
	private String manuName;
	@ApiModelProperty(value = "规格")
	private String spec;
	@ApiModelProperty(value = "图标")
	private String image;
	@ApiModelProperty(value = "描述信息")
	private String detail;
	@ApiModelProperty(value = "计量单位")
	@NotBlank(message = "计量单位不能为空")
	private String units;
	@ApiModelProperty(value = "货架号")
	private String shelf;
	@ApiModelProperty(value = "库存阈值")
	@NotNull(message = "库存阈值不能为空")
	private Integer threshold = 0;
	@ApiModelProperty(value = "最佳库存")
	@NotNull(message = "最佳库存不能为空")
	private Integer preferred = 0;
	@ApiModelProperty(value = "当前库存")
	private Integer remain = 0;
}
