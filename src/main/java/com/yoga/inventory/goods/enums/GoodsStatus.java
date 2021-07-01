package com.yoga.inventory.goods.enums;

import com.yoga.core.base.BaseEnum;

public enum GoodsStatus implements BaseEnum<String> {
    UNKNOWN("unknown", "未查询到"),
    NOTHING("nothing", "未入库"),
    NORMAL("normal", "正常");

    String code;
    String name;
    GoodsStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
}
