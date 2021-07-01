package com.yoga.inventory.barcode.enums;


import com.yoga.core.base.BaseEnum;

public enum BarcodeType implements BaseEnum<String> {
    CODE39("code39", "/code39-conf.xml"),
    CODE128("code128", "/code128-conf.xml"),
    EAN128("EAN-128", "/EAN-128-conf.xml"),
    GS1128("GS1-128", "/GS1-128-conf.xml"),
    CODEBAR("codabar", "/codabar-conf.xml"),
    UPCA("UPC-A", "/UPC-A-conf.xml"),
    UPCE("UPC-E", "/UPC-E-conf.xml"),
    EAN13("EAN-13", "/EAN-13-conf.xml"),
    EAN8("EAN-8", "/EAN-8-conf.xml");
    String code;
    String file;
    BarcodeType(String code, String file) {
        this.code = code;
        this.file = file;
    }
    public final static BarcodeType getEnum(String code) {
        for (BarcodeType t : values()) {
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
    @Override
    public String getName() {
        return file;
    }

    public static String getAllCode() {
        String result = "";
        for (BarcodeType t : values()) {
            result += t.getCode() + " ";
        }
        return result;
    }
}
