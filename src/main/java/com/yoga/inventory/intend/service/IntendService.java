package com.yoga.inventory.intend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.inventory.category.model.Category;
import com.yoga.inventory.category.service.CategoryService;
import com.yoga.inventory.goods.model.Goods;
import com.yoga.inventory.goods.service.GoodsService;
import com.yoga.inventory.intend.ao.ApplyItem;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.intend.mapper.IntendMapper;
import com.yoga.inventory.intend.model.Intend;
import com.yoga.inventory.transaction.service.TransactionService;
import com.yoga.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("inventoryIntendService")
public class IntendService extends BaseService {

	@Autowired
	private IntendMapper intendMapper;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private SettingService settingService;

	public final static String ModuleName = "inventory_output";
	public final static String Key_NoAffirm = "apply.no_affirm";

	public Intend add(long tenantId, long userId, long goodsId, OperateEnum operate, int count) {
		Intend saved = MapperQuery.create(Intend.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("userId", userId)
				.andEqualTo("goodsId", goodsId)
				.andEqualTo("operate", operate)
				.queryFirst(intendMapper);
		if (saved == null) {
			if (count < 1) throw new BusinessException("数量不能小余1！");
			Intend intend = new Intend(tenantId, userId, goodsId, operate, count);
			intendMapper.insert(intend);
			return intend;
		} else {
			saved.setCount(saved.getCount() + count);
			if (saved.getCount() < 1) throw new BusinessException("数量不能小余1！");
			saved.setAddTime(LocalDateTime.now());
			intendMapper.updateByPrimaryKey(saved);
			return saved;
		}
	}
	public Intend add(long tenantId, long userId, String barcode, OperateEnum operate, int count) {
		Goods goods = goodsService.get(tenantId, barcode, false);
		return add(tenantId, userId, goods.getId(), operate, count);
	}
	@Transactional
	public void add(long tenantId, long userId, List<ApplyItem> items, OperateEnum operate) {
		items.forEach(it-> add(tenantId, userId, it.getId(), operate, it.getCount()));
	}

	@Transactional
	public void batchAdd(long tenantId, long userId, List<String> barcodes, OperateEnum operate) {
		Map<String, Integer> goodses = new HashMap<>();
		barcodes.forEach(goods-> goodses.merge(goods, 1, Integer::sum));
		goodses.keySet().forEach(barcode-> {
			Integer count = goodses.get(barcode);
			add(tenantId, userId, barcode, operate, count);
		});
	}
    public Intend setCount(long tenantId, long userId, long goodsId, OperateEnum operate, int count) {
		Intend intend = MapperQuery.create(Intend.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("userId", userId)
				.andEqualTo("goodsId", goodsId)
				.andEqualTo("operate", operate)
				.queryFirst(intendMapper);
		if (intend == null) throw new BusinessException("修改数量失败！");
		intend.setCount(count);
		intend.setAddTime(LocalDateTime.now());
		intendMapper.updateByPrimaryKey(intend);
		return intend;
    }
    public void delete(long tenantId, long userId, long goodsId, OperateEnum operate) {
		Intend intend = new Intend(tenantId, userId, goodsId, operate, 0);
		intendMapper.deleteByPrimaryKey(intend);
    }
	public PageInfo<Intend> list(long tenantId, Long userId, String barcode, Long categoryId, Long goodsId, String keyword, OperateEnum operate, int pageIndex, int pageSize) {
		List<Long> categoryIds = null;
		if (categoryId != null && categoryId != 0L) {
			categoryIds = categoryService.getChildren(tenantId, categoryId, true).stream().map(Category::getId).collect(Collectors.toList());
		}
		PageHelper.startPage(pageIndex + 1, pageSize);
		List<Intend> intends = intendMapper.list(tenantId, userId, goodsId, barcode, categoryIds, keyword, operate);
		return new PageInfo<>(intends);
	}
	public void clear(long tenantId, long userId, OperateEnum operate) {
		new MapperQuery<>(Intend.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("userId", userId)
				.andEqualTo("operate", operate, operate != null)
				.delete(intendMapper);
	}
	public void clearInvalid(long tenantId, long userId, OperateEnum operate) {
		new MapperQuery<>(Intend.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("userId", userId)
				.andEqualTo("operate", operate, operate != null)
				.andLessThan("count", 1)
				.delete(intendMapper);
	}
	@Transactional
	public long commit(long tenantId, long userId, OperateEnum operate, List<ApplyItem> items, String remark) {
		if (CollectionUtils.isEmpty(items)) {
//			if (operate == OperateEnum.OUTPUT) {
//				List<Intend> understock = intendMapper.listUnderstock(tenantId, userId);
//				if (understock.size() > 0) {
//					StringBuilder message = new StringBuilder("下列资产库存不足，无法出库！\n");
//					understock.forEach(intend -> message.append(intend.getName()).append("[").append(intend.getBarcode()).append("]\n"));
//					throw new BusinessException(message.toString());
//				}
//			}
			List<Intend> intends = intendMapper.list(tenantId, userId, null, null, null, null, operate);
			if (intends == null || intends.size() < 1) {
				if (operate == OperateEnum.INPUT) throw new BusinessException("没有待入库资产！");
				else throw new BusinessException("没有待出库资产！");
			}
			items = intends.stream().map(it -> new ApplyItem(it.getGoodsId(), it.getCount())).collect(Collectors.toList());
		}
		if (CollectionUtils.isEmpty(items)) throw new BusinessException("请指定待操作的资产列表！");
		return transactionService.add(tenantId, userId, operate, items, remark);
	}

	public boolean isNoAffirm(long tenantId) {
		return settingService.get(tenantId, ModuleName, Key_NoAffirm, false);
	}
}
