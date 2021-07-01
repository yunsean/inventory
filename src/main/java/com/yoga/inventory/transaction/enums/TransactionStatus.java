package com.yoga.inventory.transaction.enums;

import com.yoga.core.base.BaseEnum;

public enum TransactionStatus implements BaseEnum<String> {
    pending("待确认"),
    affirmed("已确认"),;

    String name;
    TransactionStatus(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return toString();
    }
    public String getName() {
        return name;
    }
}
