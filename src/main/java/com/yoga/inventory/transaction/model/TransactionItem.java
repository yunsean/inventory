package com.yoga.inventory.transaction.model;

import com.yoga.excelkit.annotation.Excel;
import com.yoga.excelkit.annotation.ExcelField;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Excel("出入库明细")
@NoArgsConstructor
@Entity(name = "inventoryTransactionItem")
@Table(name = "stock_transaction_item")
public class TransactionItem implements Serializable {

    @Id
    @Column(name = "transaction_id")
    @ExcelField(value = "记录ID", width = 150)
    private Long transactionId;
    @Id
    @Column(name = "goods_id")
    @ExcelField(value = "资产ID", width = 150)
    private Long goodsId;
    @Column(name = "count")
    @ExcelField(value = "操作数量", width = 150)
    private Integer count;

    @Transient
    @ExcelField(value = "资产名称", width = 150)
    private String name;
    @Transient
    @ExcelField(value = "资产编码", width = 150)
    private String code;
    @Transient
    @ExcelField(value = "资产条码", width = 150)
    private String barcode;
    @Transient
    @ExcelField(value = "资产品牌", width = 150)
    private String tradeMark;
    @Transient
    @ExcelField(value = "生产厂家", width = 150)
    private String manuName;
    @Transient
    @ExcelField(value = "规格参数", width = 150)
    private String spec;
    @Transient
    private String image;
    @Transient
    @ExcelField(value = "分类ID", width = 150)
    private Long categoryId;
    @Transient
    @ExcelField(value = "分类名称", width = 150)
    private String category;

    @Transient
    @ExcelField(value = "用户ID", width = 150)
    private Long userId;
    @Transient
    @ExcelField(value = "操作状态", width = 150)
    private TransactionStatus status;
    @Transient
    @ExcelField(value = "操作类型", width = 150)
    private OperateEnum operate;
    @Transient
    @ExcelField(value = "操作时间", width = 150)
    private LocalDateTime addTime;
    @Transient
    @ExcelField(value = "备注信息", width = 150)
    private String remark;
    @Transient
    @ExcelField(value = "操作用户", width = 150)
    private String nickname;
    @Transient
    private String avatar;
    @Transient
    private Integer remain;
    @Transient
    private String units;
    @Transient
    private String shelf;

    public TransactionItem(long transactionId, long goodsId, int count) {
        this.transactionId = transactionId;
        this.goodsId = goodsId;
        this.count = count;
    }
}

