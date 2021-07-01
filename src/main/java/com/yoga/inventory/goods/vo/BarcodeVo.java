package com.yoga.inventory.goods.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.inventory.goods.enums.GoodsStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarcodeVo {

    @ApiModelProperty(value = "资产详情")
    private GoodsVo goods;

    @ApiModelProperty(value = "条码详情")
    private Object infos;

    @ApiModelProperty(value = "资产状态")
    private GoodsStatus status;

    public BarcodeVo() {
        this.status = GoodsStatus.UNKNOWN;
    }
    public BarcodeVo(GoodsVo goods) {
        this.goods = goods;
        this.status = GoodsStatus.NORMAL;
    }
    public BarcodeVo(Object infos) {
        this.infos = infos;
        this.status = GoodsStatus.NOTHING;
    }
}

