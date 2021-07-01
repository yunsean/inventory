package com.yoga.inventory.transaction.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.inventory.goods.model.Goods;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.intend.service.IntendService;
import com.yoga.inventory.intend.ao.ApplyItem;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import com.yoga.inventory.transaction.mapper.TransactionItemMapper;
import com.yoga.inventory.transaction.mapper.TransactionMapper;
import com.yoga.inventory.transaction.model.Transaction;
import com.yoga.inventory.transaction.model.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("inventoryTransactionService")
public class TransactionService extends BaseService {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionItemMapper transactionItemMapper;
    @Lazy
    @Autowired
    private IntendService intendService;

    public static String getLockName(long id) {
        return "lock.transaction." + id;
    }

    @Transactional
    public long add(long tenantId, long userId, OperateEnum operate, List<ApplyItem> items, String remark) {
        int totalCount = items.stream().mapToInt(ApplyItem::getCount).sum();
        items = new ArrayList<>(items.stream().collect(Collectors.toMap(ApplyItem::getId, a -> a, (o1, o2) -> {
            o1.setCount(o1.getCount() + o2.getCount());
            return o1;
        })).values());
        TransactionStatus status = TransactionStatus.pending;
        if (operate == OperateEnum.INPUT || intendService.isNoAffirm(tenantId)) status = TransactionStatus.affirmed;
        Transaction transaction = new Transaction(tenantId, userId, status, operate, remark, totalCount, items.size());
        transactionMapper.insert(transaction);
        List<TransactionItem> detail = items.stream().map(it-> new TransactionItem(transaction.getId(), it.getId(), it.getCount())).collect(Collectors.toList());
        transactionItemMapper.insertItems(detail);
        try {
            transactionItemMapper.updateGoods(transaction.getId(), operate);
        } catch (DataIntegrityViolationException ex) {
            List<Goods> understock = transactionItemMapper.understockGoods(transaction.getId());
            if (!CollectionUtils.isEmpty(understock)) {
                StringBuilder sb = new StringBuilder("下列物品库存不足：\n");
                understock.forEach(it-> sb.append(it.getName()).append("\n"));
                sb.append("无法提交出库！");
                throw new BusinessException(sb.toString());
            } else {
                throw ex;
            }
        }
        transactionItemMapper.updateIntend(transaction.getId(), tenantId, userId, operate);
        intendService.clearInvalid(tenantId, userId, operate);
        return transaction.getId();
    }
    @Transactional
    public long add(long tenantId, long userId, OperateEnum operate, long goodsId, int count, String remark) {
        List<ApplyItem> items = new ArrayList<>();
        items.add(new ApplyItem(goodsId, count));
        return add(tenantId, userId, operate, items, remark);
    }

    public PageInfo<Transaction> list(long tenantId, LocalDate beginDate, LocalDate endDate, OperateEnum operate, TransactionStatus status, Long userId, String user, Long goodsId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Transaction> transactions = transactionMapper.list(tenantId, userId, user, goodsId, beginDate, endDate, operate, status);
        return new PageInfo<>(transactions);
    }
    public List<Transaction> list(long tenantId, LocalDate beginDate, LocalDate endDate, OperateEnum operate, TransactionStatus status, Long userId, String user, Long goodsId) {
        List<Transaction> transactions = transactionMapper.list(tenantId, userId, user, goodsId, beginDate, endDate, operate, status);
        return transactions;
    }
    public Transaction get(long tenantId, long transactionId) {
        Transaction transaction = transactionMapper.get(tenantId, transactionId);
        if (transaction == null) throw new BusinessException("未找到该记录！");
        return transaction;
    }

    public PageInfo<TransactionItem> detail(long tenantId, Long transactionId, Long userId, Long goodsId, OperateEnum operate, LocalDate beginDate, LocalDate endDate, String barcode, String keyword, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<TransactionItem> details = transactionItemMapper.list(tenantId, transactionId, userId, goodsId, operate, beginDate, endDate, barcode, keyword);
        return new PageInfo<>(details);
    }
    public List<TransactionItem> detail(long tenantId, Long transactionId, Long userId, Long goodsId, OperateEnum operate, LocalDate beginDate, LocalDate endDate, String barcode, String keyword) {
        List<TransactionItem> details = transactionItemMapper.list(tenantId, transactionId, userId, goodsId, operate, beginDate, endDate, barcode, keyword);
        return details;
    }

    public void affirm(long tenantId, long id) {
        Transaction transaction = transactionMapper.selectByPrimaryKey(id);
        if (transaction == null || transaction.getTenantId() != tenantId) throw new BusinessException("申请记录不存在！");
        if (transaction.getStatus() != TransactionStatus.pending) throw new BusinessException("申请无法被确认或者已经被取消！");
        Transaction updated = new Transaction(id, TransactionStatus.affirmed);
        transactionMapper.updateByPrimaryKeySelective(updated);
    }
    @Transactional
    public void cancel(long tenantId, long id, Long userId) {
        Transaction transaction = transactionMapper.selectByPrimaryKey(id);
        if (transaction == null || transaction.getTenantId() != tenantId) throw new BusinessException("申请记录不存在！");
        if (transaction.getStatus() != TransactionStatus.pending) throw new BusinessException("当前状态的申请不能被取消！");
        if (userId != null && !userId.equals(transaction.getUserId())) throw new BusinessException("只能取消自己的申请！");
        List<TransactionItem> items = MapperQuery.create(TransactionItem.class)
                .andEqualTo("transactionId", id)
                .query(transactionItemMapper);
        if (!CollectionUtils.isEmpty(items)) {
            List<ApplyItem> applyItems = items.stream().map(it -> new ApplyItem(it.getGoodsId(), it.getCount())).collect(Collectors.toList());
            intendService.add(tenantId, transaction.getUserId(), applyItems, transaction.getOperate());
            transactionItemMapper.returnGoods(id);
            MapperQuery.create(TransactionItem.class)
                    .andEqualTo("transactionId", id)
                    .delete(transactionItemMapper);
        }
        transactionMapper.deleteByPrimaryKey(id);
    }
}
