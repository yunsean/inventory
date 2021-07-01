package com.yoga.inventory.intend.enums;

import com.yoga.core.base.BaseEnum;

public enum OperateEnum implements BaseEnum<String> {
    INPUT("input", "入库"),
    OUTPUT("output", "出库"),;
    String code;
    String name;
    OperateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public final static OperateEnum getEnum(String code) {
        for (OperateEnum t : values()) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
}
