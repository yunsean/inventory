package com.yoga.inventory.transaction.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import com.yoga.inventory.transaction.model.Transaction;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionMapper extends MyMapper<Transaction> {
    List<Transaction> list(@Param("tenantId") long tenantId,
                           @Param("userId") Long userId,
                           @Param("user") String user,
                           @Param("goodsId") Long goodsId,
                           @Param("beginDate") LocalDate beginDate,
                           @Param("endDate") LocalDate endDate,
                           @Param("operate") OperateEnum operate,
                           @Param("status") TransactionStatus status);
    Transaction get(@Param("tenantId") long tenantId,
                    @Param("id") long id);
}
