package com.yoga.inventory.transaction.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.excelkit.ExcelKit;
import com.yoga.inventory.category.service.CategoryService;
import com.yoga.inventory.intend.enums.OperateEnum;
import com.yoga.inventory.transaction.dto.*;
import com.yoga.inventory.transaction.enums.TransactionStatus;
import com.yoga.inventory.transaction.model.Transaction;
import com.yoga.inventory.transaction.model.TransactionItem;
import com.yoga.inventory.transaction.service.TransactionService;
import com.yoga.inventory.transaction.vo.TransactionItemVo;
import com.yoga.inventory.transaction.vo.TransactionVo;
import com.yoga.operator.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "出入库记录")
@Controller("inventoryTransactionController")
@RequestMapping("/admin/inventory/transaction")
public class TransactionController extends BaseController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;

    @RequiresAuthentication
    @RequestMapping("")
    public String list(ModelMap model, CommonPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Transaction> transactions = transactionService.list(dto.getTid(), dto.getBeginDate(), dto.getEndDate(), dto.getOperate(), dto.getStatus(), dto.getUserId(), dto.getUser(), dto.getGoodsId(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("transactions", transactions.getList());
        model.put("page", new CommonPage(transactions));
        model.put("operates", OperateEnum.values());
        model.put("statuses", TransactionStatus.values());
        return "/admin/inventory/transaction/list";
    }
    @RequiresPermissions("inventory_affirm")
    @RequestMapping("/affirm")
    public String affirm(ModelMap model, CommonPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        Subject subject = SecurityUtils.getSubject();
        Long userId = subject.isPermitted("inventory_affirm.affirm") ? dto.getUserId() : user.getId();
        TransactionStatus status = subject.isPermitted("inventory_affirm.affirm") ? TransactionStatus.pending : null;
        PageInfo<Transaction> transactions = transactionService.list(dto.getTid(), dto.getBeginDate(), dto.getEndDate(), OperateEnum.OUTPUT, status, userId, null, dto.getGoodsId(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("page", new CommonPage(transactions));
        model.put("transactions", transactions.getList());
        model.put("user", user);
        return "/admin/inventory/transaction/affirm";
    }
    @RequiresAuthentication
    @RequestMapping("/journal")
    public String journal(ModelMap model, CommonPage page, @Valid JournalDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<TransactionItem> items = transactionService.detail(dto.getTid(), dto.getId(), dto.getUserId(), dto.getGoodsId(), dto.getOperate(), dto.getBeginDate(), dto.getEndDate(), dto.getBarcode(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("details", items.getList());
        model.put("page", new CommonPage(items));
        model.put("operates", OperateEnum.values());
        model.put("statuses", TransactionStatus.values());
        return "/admin/inventory/transaction/journal";
    }
    @RequiresAuthentication
    @RequestMapping("/detail")
    public String detail(ModelMap model, CommonPage page, @Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Transaction transaction = transactionService.get(dto.getTid(), dto.getId());
        PageInfo<TransactionItem> items = transactionService.detail(dto.getTid(), dto.getId(), null, null, null, null, null, null, null, page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("transaction", transaction);
        model.put("details", items.getList());
        model.put("page", new CommonPage(items));
        model.put("operates", OperateEnum.values());
        return "/admin/inventory/transaction/detail";
    }
    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("inventory_transaction")
    @RequestMapping("/journal/export")
    public void exportExcel(HttpServletResponse response, @Valid JournalDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<TransactionItem> items = transactionService.detail(dto.getTid(), dto.getId(), dto.getUserId(), dto.getGoodsId(), dto.getOperate(), dto.getBeginDate(), dto.getEndDate(), dto.getBarcode(), dto.getKeyword());
        ExcelKit.$Export(TransactionItem.class, response).downXlsx(items, false);
    }
    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("inventory_journal")
    @RequestMapping("/export")
    public void exportExcel(HttpServletResponse response, @Valid ListDto dto, BindingResult bindingResult) {
        List<Transaction> transactions = transactionService.list(dto.getTid(), dto.getBeginDate(), dto.getEndDate(), dto.getOperate(), dto.getStatus(), dto.getUserId(), dto.getUser(), dto.getGoodsId());
        ExcelKit.$Export(Transaction.class, response).downXlsx(transactions, false);
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/list.json")
    @ApiOperation("出入库历史列表")
    public ApiResults<TransactionVo> list(@ModelAttribute CommonPage page, @Valid @ModelAttribute ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Transaction> transactions = transactionService.list(dto.getTid(), dto.getBeginDate(), dto.getEndDate(), dto.getOperate(), dto.getStatus(), dto.getUserId(), dto.getUser(), dto.getGoodsId(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(transactions, TransactionVo.class, (po, vo)-> {
            vo.setStatusName(po.getStatus().getName());
            vo.setOperateName(po.getOperate().getName());
        });
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/get.json")
    @ApiOperation("出入库历史详情")
    public ApiResult<TransactionVo> get(@Valid @ModelAttribute GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Transaction transaction = transactionService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(transaction, TransactionVo.class, (po, vo)-> {
            vo.setStatusName(po.getStatus().getName());
            vo.setOperateName(po.getOperate().getName());
        });
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/detail.json")
    @ApiOperation("出入库资产清单")
    public ApiResults<TransactionItemVo> detail(@ModelAttribute CommonPage page, @Valid @ModelAttribute JournalDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<TransactionItem> items = transactionService.detail(dto.getTid(), dto.getId(), dto.getUserId(), dto.getGoodsId(), dto.getOperate(), dto.getBeginDate(), dto.getEndDate(), dto.getBarcode(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(items, TransactionItemVo.class, (po, vo)-> {
            vo.setStatusName(po.getStatus().getName());
            vo.setOperateName(po.getOperate().getName());
        });
    }


    @ResponseBody
    @RequiresPermissions("inventory_affirm.affirm")
    @PostMapping("/affirm.json")
    @ApiOperation("确认出库")
    public ApiResult affirm(@Valid @ModelAttribute AffirmDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        runInLock(TransactionService.getLockName(dto.getId()), ()-> {
            transactionService.affirm(dto.getTid(), dto.getId());
        });
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/cancel.json")
    @ApiOperation("取消出库申请")
    public ApiResult cancel(@Valid @ModelAttribute CancelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        Subject subject = SecurityUtils.getSubject();
        Long userId = subject.isPermitted("inventory_affirm.affirm") ? null : user.getId();
        runInLock(TransactionService.getLockName(dto.getId()), ()-> {
            transactionService.cancel(dto.getTid(), dto.getId(), userId);
        });
        return new ApiResult();
    }
}
