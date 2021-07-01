package com.yoga.inventory.transaction.model;

import com.yoga.excelkit.annotation.Excel;
import com.yoga.excelkit.annotation.ExcelField;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Excel("出入库历史")
@NoArgsConstructor
@Entity(name = "inventoryTransaction")
@Table(name = "stock_transaction")
public class Transaction {

    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelField(value = "ID", width = 150)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "user_id")
    @ExcelField(value = "用户ID", width = 150)
    private Long userId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ExcelField(value = "操作状态", width = 150)
    private TransactionStatus status;
    @Column(name = "operate")
    @Enumerated(EnumType.STRING)
    @ExcelField(value = "操作类型", width = 150)
    private OperateEnum operate;
    @Column(name = "remark")
    @ExcelField(value = "备注信息", width = 150)
    private String remark;
    @Column(name = "total_count")
    @ExcelField(value = "操作总数", width = 150)
    private Integer totalCount;
    @Column(name = "kind_count")
    @ExcelField(value = "品类总数", width = 150)
    private Integer kindCount;
    @Column(name = "add_time")
    @ExcelField(value = "操作时间", width = 150)
    private LocalDateTime addTime;

    @Transient
    @ExcelField(value = "操作用户", width = 150)
    private String nickname;
    @Transient
    private String avatar;

    public Transaction(long tenantId, long userId, TransactionStatus status, OperateEnum operate, String remark, int totalCount, int kindCount) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.status = status;
        this.operate = operate;
        this.remark = remark;
        this.totalCount = totalCount;
        this.kindCount = kindCount;
        this.addTime = LocalDateTime.now();
    }

    public Transaction(Long id, TransactionStatus status) {
        this.id = id;
        this.status = status;
    }
}

