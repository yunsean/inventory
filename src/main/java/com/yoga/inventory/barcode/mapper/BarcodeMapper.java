package com.yoga.inventory.barcode.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.inventory.barcode.model.Barcode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BarcodeMapper extends MyMapper<Barcode> {
}
