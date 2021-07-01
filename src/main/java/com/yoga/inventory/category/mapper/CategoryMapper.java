package com.yoga.inventory.category.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.inventory.category.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "inventoryCategoryMapper")
public interface CategoryMapper extends MyMapper<Category> {
    List<Category> findChildren(@Param("tenantId") long tenantId,
                                @Param("parentId") long parentId,
                                @Param("includeParentId") boolean includeParentId);
}
