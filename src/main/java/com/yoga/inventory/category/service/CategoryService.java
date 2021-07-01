package com.yoga.inventory.category.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.inventory.category.mapper.CategoryMapper;
import com.yoga.inventory.category.model.Category;
import com.yoga.inventory.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service("inventoryCategoryService")
public class CategoryService extends BaseService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodsService goodsService;

    public long add(long tenantId, String name, String remark, long parentId, String image) {
        if (MapperQuery.create(Category.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("parentId", parentId)
                .andEqualTo("name", name)
                .exist(categoryMapper)) throw new BusinessException("已存在同名分类！");
        if (parentId != 0 && !MapperQuery.create(Category.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("id", parentId)
                .exist(categoryMapper)) throw new BusinessException("父分类不存在！");
        Category category = new Category(tenantId, name, remark, parentId, image);
        categoryMapper.insert(category);
        return category.getId();
    }
    public void delete(long tenantId, long id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null || category.getTenantId() != tenantId) throw new BusinessException("未找到该分类！");
        if (MapperQuery.create(Category.class)
                .andEqualTo("parentId", id)
                .exist(categoryMapper)) throw new BusinessException("该分类包含子目录，无法删除！");
        if (!goodsService.categoryIsEmpty(id)) throw new BusinessException("该分类包含资产对象，无法删除！");    //TODO
        categoryMapper.deleteByPrimaryKey(id);
    }
    public void update(long tenantId, long id, String name, String remark, Long parentId, String image) {
        Category saved = categoryMapper.selectByPrimaryKey(id);
        if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("未找到该分类！");
        if (parentId != null && !parentId.equals(saved.getParentId())) {
            if (parentId != 0) {
                Category parent = categoryMapper.selectByPrimaryKey(parentId);
                if (parent == null) throw new BusinessException("父分类不存在！");
            }
            if (parentId == id) throw new BusinessException("不能指定自己为父分类！");
            saved.setParentId(parentId);
        }
        if (StringUtil.isNotBlank(name)) {
            Category category = MapperQuery.create(Category.class)
                    .andEqualTo("parentId", saved.getParentId())
                    .andEqualTo("name", name)
                    .queryFirst(categoryMapper);
            if (category != null && category.getId() != saved.getId()) throw new BusinessException("已存在同名分类！");
            saved.setName(name);
        }
        if (remark != null) saved.setRemark(remark);
        if (image != null) saved.setImage(image);
        categoryMapper.updateByPrimaryKey(saved);
    }
    public boolean exist(long tenantId, Long id) {
        return MapperQuery.create(Category.class)
                .andEqualTo("id", id)
                .andEqualTo("tenantId", tenantId)
                .exist(categoryMapper);
    }
    public List<Category> find(long tenantId, Long parentId, String name) {
        return MapperQuery.create(Category.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("parentId", parentId, parentId != null)
                .andLike("name", "%" + name + "%", StringUtil.isNotBlank(name))
                .orderBy("id")
                .query(categoryMapper);
    }
    private List<Category> composeColumn(List<Category> columns, boolean resort) {
        if (resort) columns.sort(Comparator.comparing(Category::getName));
        Map<Long, Category> mapColumns = new HashMap<>();
        for (Category cmsColumn : columns) {
            mapColumns.put(cmsColumn.getId(), cmsColumn);
        }
        Iterator<Entry<Long, Category>> it = mapColumns.entrySet().iterator();
        List<Category> result = new ArrayList<>();
        while (it.hasNext()) {
            Category self = it.next().getValue();
            Category parent = mapColumns.get(self.getParentId());
            if (parent != null) {
                parent.addChild(self);
            } else {
                result.add(self);
            }
        }
        return result;
    }

    public Category get(long tenantId, long id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null || category.getTenantId() != tenantId) throw new BusinessException("未找到该分类！");
        return category;
    }
    public List<Category> all(long tenantId) {
        List<Category> columns = MapperQuery.create(Category.class)
                .andEqualTo("tenantId", tenantId)
                .query(categoryMapper);
        return composeColumn(columns, true);
    }
    public List<Category> getChildren(long tenantId, long parentId, boolean includeItself) {
        if (parentId != 0L) {
            Category saved = categoryMapper.selectByPrimaryKey(parentId);
            if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("未找到该分类！");
        }
        return categoryMapper.findChildren(tenantId, parentId, includeItself);
    }
    public long count(long tenantId) {
        return MapperQuery.create(Category.class)
                .andEqualTo("tenantId", tenantId)
                .count(categoryMapper);
    }
}
