package com.yoga.inventory.intend.model;

import com.yoga.inventory.intend.enums.OperateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "inventoryIntendItem")
@Table(name = "stock_intend_item")
public class Intend implements Serializable {

    @Id
    @Column(name = "tenant_id")
    private Long tenantId;
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    @Column(name = "goods_id")
    private Long goodsId;
    @Id
    @Column(name = "operate")
    @Enumerated(EnumType.STRING)
    private OperateEnum operate;
    @Column(name = "count")
    private Integer count;
    @Column(name = "add_time")
    private LocalDateTime addTime;

    @Transient
    private String barcode;
    @Transient
    private String code;
    @Transient
    private String name;
    @Transient
    private String tradeMark;
    @Transient
    private String manuName;
    @Transient
    private String spec;
    @Transient
    private String image;
    @Transient
    private Long categoryId;
    @Transient
    private String category;
    @Transient
    private Integer remain;
    @Transient
    private String units;

    public Intend(long tenantId, long userId, long goodsId, OperateEnum operate, int count) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.goodsId = goodsId;
        this.operate = operate;
        this.count = count;
        this.addTime = LocalDateTime.now();
    }
}

