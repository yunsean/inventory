package com.yoga.inventory.barcode.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "inventoryBarcode")
@Table(name = "stock_barcode")
public class Barcode implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "barcode")
    private String barcode;
    @Column(name = "detail")
    private String detail;
    @Column(name = "latest_query")
    private LocalDateTime latestQuery;
    @Column(name = "invalid")
    private Boolean invalid;

    public Barcode(String barcode, String detail, LocalDateTime latestQuery) {
        this.barcode = barcode;
        this.detail = detail;
        this.latestQuery = latestQuery;
        this.invalid = false;
    }
}

