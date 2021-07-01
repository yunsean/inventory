package com.yoga.inventory.goods.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.inventory.goods.model.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends MyMapper<Goods> {
    List<Goods> list(@Param("tenantId") long tenantId,
                     @Param("id") Long id,
                     @Param("categoryIds") List<Long> categoryIds,
                     @Param("barcode") String barcode,
                     @Param("name") String name,
                     @Param("keyword") String keyword,
                     @Param("remainLess") Boolean remainLess,
                     @Param("preferAdd") Boolean preferAdd);
}
