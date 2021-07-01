package com.yoga.inventory.category.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "inventoryCatelog")
@Table(name = "stock_category")
public class Category implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "name")
    private String name;
    @Column(name = "remark")
    private String remark;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "image")
    private String image;

    @Transient
    private Set<Category> children = null;

    public Category(long tenantId, String name, String remark, long parentId, String image) {
        this.tenantId = tenantId;
        this.name = name;
        this.remark = remark;
        this.parentId = parentId;
        this.image = image;
    }

    public void addChild(Category category) {
        if (this.children == null) this.children = new HashSet<>();
        this.children.add(category);
    }
}

