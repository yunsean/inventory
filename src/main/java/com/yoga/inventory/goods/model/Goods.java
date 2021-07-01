package com.yoga.inventory.goods.model;

import com.yoga.excelkit.annotation.Excel;
import com.yoga.excelkit.annotation.ExcelField;
import com.yoga.excelkit.convert.ExcelDateConvertor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Excel("资产列表")
@NoArgsConstructor
@Entity(name = "inventoryGoods")
@Table(name = "stock_goods")
public class Goods {

    @Id
    @ExcelField(value = "ID", width = 150)
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @ExcelField(value = "分类ID", width = 150)
    @Column(name = "category_id")
    private Long categoryId;
    @ExcelField(value = "资产条码", width = 150)
    @Column(name = "barcode")
    private String barcode;
    @ExcelField(value = "资产编码", width = 150)
    @Column(name = "code")
    private String code;
    @ExcelField(value = "资产名称", width = 150)
    @Column(name = "name")
    private String name;
    @Column(name = "spell")
    private String spell;
    @Column(name = "initial")
    private String initial;
    @ExcelField(value = "资产品牌", width = 150)
    @Column(name = "trade_mark")
    private String tradeMark;
    @ExcelField(value = "生产厂家", width = 350)
    @Column(name = "manu_name")
    private String manuName;
    @ExcelField(value = "规格参数", width = 250)
    @Column(name = "spec")
    private String spec;
    @Column(name = "image")
    private String image;
    @Column(name = "detail")
    private String detail;
    @ExcelField(value = "计量单位", width = 150)
    @Column(name = "units")
    private String units;
    @ExcelField(value = "货架号", width = 150)
    @Column(name = "shelf")
    private String shelf;

    @ExcelField(value = "创建日期", width = 150, writeConverter = ExcelDateConvertor.class)
    @Column(name = "add_time")
    private LocalDateTime addTime;
    @ExcelField(value = "最后修改", width = 150, writeConverter = ExcelDateConvertor.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "deleted")
    private Boolean deleted;

    @ExcelField(value = "库存阈值", width = 150)
    @Column(name = "threshold")
    private Integer threshold;
    @ExcelField(value = "最佳入库", width = 150)
    @Column(name = "preferred")
    private Integer preferred;
    @ExcelField(value = "当前库存", width = 150)
    @Column(name = "remain")
    private Integer remain;
    @ExcelField(value = "总消耗量", width = 150)
    @Column(name = "consumed")
    private Integer consumed;
    @ExcelField(value = "总入库量", width = 150)
    @Column(name = "inputted")
    private Integer inputted;

    @Transient
    @ExcelField(value = "资产品类", width = 150)
    private String category;

    public Goods(Long tenantId, Long categoryId, String barcode, String code, String name, String spell, String initial, String tradeMark, String manuName, String spec, String image, String detail, String units, String shelf, Integer threshold, Integer preferred, Integer remain) {
        this.tenantId = tenantId;
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.code = code;
        this.name = name;
        this.spell = spell;
        this.initial = initial;
        this.tradeMark = tradeMark;
        this.manuName = manuName;
        this.spec = spec;
        this.image = image;
        this.detail = detail;
        this.units = units;
        this.shelf = shelf;

        this.addTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.deleted = false;

        this.threshold = threshold;
        this.preferred = preferred;
        this.remain = remain;
        this.consumed = 0;
        this.inputted = 0;
    }

    public Goods(Long id) {
        this.id = id;
    }

    public Goods(Long id, String barcode) {
        this.id = id;
        this.barcode = barcode;
    }
}

