package com.yoga.inventory.goods.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.inventory.category.model.Category;
import com.yoga.inventory.category.service.CategoryService;
import com.yoga.inventory.goods.mapper.GoodsMapper;
import com.yoga.inventory.goods.model.Goods;
import com.yoga.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("inventoryGoodsService")
public class GoodsService extends BaseService {

	@Lazy
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private SettingService settingService;

	public final static String ModuleName = "inventory_goods";
	public final static String Key_AutoGenCode = "key.code-prefix";

	@Transactional
	public long add(long tenantId, long categoryId, String barcode, String code, String name, String spell, String initial, String tradeMark, String manuName, String spec, String image, String detail, String units, String shelf, Integer threshold, Integer preferred, Integer remain) {
		if (StringUtil.isNotBlank(barcode)) {
			long count = MapperQuery.create(Goods.class)
					.andEqualTo("tenantId", tenantId)
					.andEqualTo("barcode", barcode)
					.andEqualTo("deleted", 0)
					.count(goodsMapper);
			if (count > 0) throw new BusinessException("已经存在相同条码的资产！");
		}
		if (StringUtil.isNotBlank(code)) {
			long count = MapperQuery.create(Goods.class)
					.andEqualTo("tenantId", tenantId)
					.andEqualTo("code", code)
					.andEqualTo("deleted", 0)
					.count(goodsMapper);
			if (count > 0) throw new BusinessException("已经存在相同编码的资产！");
		}
		if (categoryId != -1 && categoryId != 0 && !categoryService.exist(tenantId, categoryId)) throw new BusinessException("指定分类不存在！");
		Goods goods = new Goods(tenantId, categoryId, barcode, code, name, spell, initial, tradeMark, manuName, spec, image, detail, units, shelf, threshold, preferred, remain);
		goodsMapper.insert(goods);
		boolean needUpdate = false;
		Goods update = new Goods(goods.getId());
		if (StringUtil.isBlank(barcode)) {
			long number = 99999999999L - goods.getId();
			update.setBarcode(String.valueOf(number));
			needUpdate = true;
		}
		if (StringUtil.isBlank(code)) {
			String prefix = settingService.get(tenantId, ModuleName, Key_AutoGenCode, "");
			if (StringUtil.isNotBlank(prefix)) {
				String suffix = String.valueOf(goods.getId());
				if (suffix.length() >= prefix.length()) update.setCode(suffix);
				else update.setCode(prefix.substring(0, prefix.length() - suffix.length()) + suffix);
				needUpdate = true;
			}
		}
		if (needUpdate) goodsMapper.updateByPrimaryKeySelective(update);
		return goods.getId();
	}
	public void delete(long tenantId, long id) {
		Goods goods = goodsMapper.selectByPrimaryKey(id);
		if (goods == null || goods.getTenantId() != tenantId || goods.getDeleted()) throw new BusinessException(-11, "未找到该资产！");
		Goods updated = new Goods(id);
		updated.setDeleted(true);
		goodsMapper.updateByPrimaryKeySelective(updated);
	}
	public void update(long tenantId, long id, Long categoryId, String barcode, String code, String name, String spell, String initial, String tradeMark, String manuName, String spec, String image, String detail, String units, String shelf, Integer threshold, Integer preferred, Integer remain, Integer consumed, Integer inputted) {
		Goods saved = goodsMapper.selectByPrimaryKey(id);
		if (saved == null || saved.getTenantId() != tenantId || saved.getDeleted()) throw new BusinessException(-11, "未找到该资产！");
		if (categoryId != null && !categoryId.equals(saved.getCategoryId())) {
			if (!categoryService.exist(tenantId, categoryId)) throw new BusinessException("指定分类不存在！");
            saved.setCategoryId(categoryId);
		}
		if (StringUtil.isNotBlank(barcode)) {
			Goods goods = MapperQuery.create(Goods.class)
					.andEqualTo("tenantId", tenantId)
					.andEqualTo("barcode", barcode)
					.andEqualTo("deleted", 0)
					.queryFirst(goodsMapper);
			if (goods != null && goods.getId() != saved.getId()) throw new BusinessException("已经存在相同条码的资产！");
			saved.setBarcode(barcode);
		}
		if (code != null) {
			if (StringUtil.isNotBlank(code)) {
				Goods goods = MapperQuery.create(Goods.class)
						.andEqualTo("tenantId", tenantId)
						.andEqualTo("code", code)
						.andEqualTo("deleted", 0)
						.queryFirst(goodsMapper);
				if (goods != null && goods.getId() != saved.getId()) throw new BusinessException("已经存在相同编码的资产！");
			}
			saved.setCode(code);
		}
		if (StringUtil.isNotBlank(name)) saved.setName(name);
		if (StringUtil.isNotBlank(spell)) saved.setSpell(spell);
		if (StringUtil.isNotBlank(initial)) saved.setInitial(initial);
		if (tradeMark != null) saved.setTradeMark(tradeMark);
		if (manuName != null) saved.setManuName(manuName);
		if (spec != null) saved.setSpec(spec);
		if (image != null) saved.setImage(image);
		if (detail != null) saved.setDetail(detail);
		if (units != null) saved.setUnits(units);
		if (shelf != null) saved.setShelf(shelf);
		if (threshold != null) saved.setThreshold(threshold);
		if (preferred != null) saved.setPreferred(preferred);
		if (consumed != null) saved.setConsumed(consumed);
		if (remain != null) saved.setRemain(remain);
		if (inputted != null) saved.setInputted(inputted);
		goodsMapper.updateByPrimaryKey(saved);
	}
	public boolean categoryIsEmpty(long categoryId) {
		return !MapperQuery.create(Goods.class)
				.andEqualTo("categoryId", categoryId)
				.andEqualTo("deleted", 0)
				.exist(goodsMapper);
	}
	public Goods get(long tenantId, String barcode, boolean allowNull) {
		List<Goods> goodses = goodsMapper.list(tenantId, null, null, barcode, null, null, null, null);
		if (!allowNull && goodses.size() < 1) throw new BusinessException(-11, "未找到该资产！");
		else if (goodses.size() < 1) return null;
		return goodses.get(0);
	}
	public Goods get(long tenantId, long id) {
		List<Goods> goodses = goodsMapper.list(tenantId, id, null, null, null, null, null, null);
		if (goodses.size() < 1) throw new BusinessException(-11, "未找到该资产！");
		return goodses.get(0);
	}
	public PageInfo<Goods> list(long tenantId, Long categoryId, String barcode, String name, String keyword, Boolean remainLess, Boolean preferAdd, int pageIndex, int pageSize) {
		List<Long> categoryIds = null;
		if (categoryId != null && categoryId != 0L) {
			categoryIds = categoryService.getChildren(tenantId, categoryId, true).stream().map(Category::getId).collect(Collectors.toList());
		}
		PageHelper.startPage(pageIndex + 1, pageSize);
		List<Goods> goodses = goodsMapper.list(tenantId, null, categoryIds, barcode, name, keyword, remainLess, preferAdd);
		return new PageInfo<>(goodses);
	}
	public List<Goods> list(long tenantId, Long categoryId, String barcode, String name, String keyword, Boolean remainLess, Boolean preferAdd) {
		List<Long> categoryIds = null;
		if (categoryId != null && categoryId != 0L) {
			categoryIds = categoryService.getChildren(tenantId, categoryId, true).stream().map(Category::getId).collect(Collectors.toList());
		}
		return goodsMapper.list(tenantId, null, categoryIds, barcode, name, keyword, remainLess, preferAdd);
	}
}
