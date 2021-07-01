package com.yoga.inventory.goods.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UpdateDto extends BaseDto {

	@ApiModelProperty(value = "资产ID")
	@NotNull(message = "资产ID不能为空")
	private Long id;
	@ApiModelProperty(value = "所属分类")
	private Long categoryId;
	@ApiModelProperty(value = "资产条码，为空则自动生成")
	private String barcode;
	@ApiModelProperty(value = "资产编码")
	private String code;
	@ApiModelProperty(value = "资产名称")
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
	private String units;
	@ApiModelProperty(value = "货架号")
	private String shelf;

	@ApiModelProperty(value = "库存阈值")
	private Integer threshold;
	@ApiModelProperty(value = "最佳库存")
	private Integer preferred;
	@ApiModelProperty(value = "当前库存")
	private Integer remain;
	@ApiModelProperty(value = "总消耗量")
	private Integer consumed;
	@ApiModelProperty(value = "总入库量")
	private Integer inputted;
}
