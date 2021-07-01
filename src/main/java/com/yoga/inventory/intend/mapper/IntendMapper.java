package com.yoga.inventory.intend.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.intend.model.Intend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IntendMapper extends MyMapper<Intend> {

    Intend get(@Param("tenantId") long tenantId,
               @Param("userId") long userId,
               @Param("goodsId") Long goodsId,
               @Param("barcode") String barcode,
               @Param("operate") OperateEnum operate);
    List<Intend> list(@Param("tenantId") long tenantId,
                      @Param("userId") Long userId,
                      @Param("goodsId") Long goodsId,
                      @Param("barcode") String barcode,
                      @Param("categoryIds") List<Long> categoryIds,
                      @Param("keyword") String keyword,
                      @Param("operate") OperateEnum operate);
    List<Intend> listUnderstock(@Param("tenantId") long tenantId,
                                @Param("userId") long userId);
}
