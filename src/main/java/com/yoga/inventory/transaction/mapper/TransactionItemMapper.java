package com.yoga.inventory.transaction.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.inventory.goods.model.Goods;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.model.TransactionItem;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface TransactionItemMapper extends MyMapper<TransactionItem> {
    List<TransactionItem> list(@Param("tenantId") long tenantId,
                               @Param("transactionId") Long transactionId,
                               @Param("userId") Long userId,
                               @Param("goodsId") Long goodsId,
                               @Param("operate") OperateEnum operate,
                               @Param("beginDate") LocalDate beginDate,
                               @Param("endDate") LocalDate endDate,
                               @Param("barcode") String barcode,
                               @Param("keyword") String keyword);
    void updateGoods(@Param("transactionId") long transactionId,
                     @Param("operate") OperateEnum operate);
    void returnGoods(@Param("transactionId") long transactionId);
    List<Goods> understockGoods(@Param("transactionId") long transactionId);
    void updateIntend(@Param("transactionId") long transactionId,
                      @Param("tenantId") long tenantId,
                      @Param("userId") long userId,
                      @Param("operate") OperateEnum operate);
    void insertItems(@Param("list") Collection<TransactionItem> list);
}
