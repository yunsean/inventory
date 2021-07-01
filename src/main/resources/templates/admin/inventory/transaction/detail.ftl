<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true includeUploader=true>
        <style>
            .checked {
                background-color: #eee;
            }
            td {
                vertical-align: middle!important;
            }
            .red {
                color: red!important;
                font-weight: bold;
            }
            textarea {
                min-height: 200px!important;
            }
            .small {
                font-size: small;
                color: #999;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="库存管理" icon="icon-user">
            <@crumbItem href="#" name="出入库查询" backLevel=1/>
            <@crumbItem href="#" name="出入库详情"/>
            <@backButton />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "出入库详情" />
                <@panelBody>
                    <@panel class="panel-default">
                        <@panelHeading "详情" />
                        <table class="table table-striped table-bordered table-hover" id="orderInfoTable">
                            <colgroup width="15%"></colgroup>
                            <colgroup width="35%"></colgroup>
                            <colgroup width="15%"></colgroup>
                            <colgroup width="35%"></colgroup>
                            <tbody>
                            <tr>
                                <td>
                                    操作类型：
                                </td>
                                <td>
                                    ${transaction.operate.getName()?if_exists}
                                </td>
                                <td>
                                    操作状态：
                                </td>
                                <td>
                                    ${transaction.status.getName()?if_exists}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    操作日期：
                                </td>
                                <td>
                                    ${(transaction.addTime?string("yyyy-MM-dd HH:mm"))!}
                                </td>
                                <td>
                                    操作者：
                                </td>
                                <td>
                                    ${(transaction.nickname)!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    品类数量：
                                </td>
                                <td>
                                    ${(transaction.kindCount)!}种
                                </td>
                                <td>
                                    总数量：
                                </td>
                                <td>
                                    ${(transaction.totalCount)!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    备注信息：
                                </td>
                                <td colspan=3>
                                    ${(transaction.remark)!}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </@panel>
                    <@panel class="panel-info">
                        <div class="panel-heading">
                            清单
                            <div style="display: inline-block; float: right; margin-top: -3px">
                                <a class="btn btn-success" type="button" href="/admin/inventory/transaction/journal/export?id=${(param.id?c)!}" >
                                    <i class="icon icon-download"></i>导出
                                </a>
                            </div>
                        </div>
                        <@table>
                            <@thead>
                                <@tr>
                                    <@th 5>#</@th>
                                    <@th 30>资产名称/规格</@th>
                                    <@th 30>条码/编码</@th>
                                    <@th 10 true>货架</@th>
                                    <@th 8 true>库存</@th>
                                    <@th 8 true>数量</@th>
                                </@tr>
                            </@thead>
                            <@tbody>
                                <#list details! as item>
                                    <tr>
                                        <@td>
                                            <a href="/admin/inventory/goods/detail?id=${(item.goodsId?c)!}">${item.goodsId!}</a>
                                        </@td>
                                        <@td>
                                            ${item.name!}
                                            <div class="small">${item.spec!}</div>
                                        </@td>
                                        <@td>
                                            ${item.barcode!}
                                            <div class="small">${item.code!}</div>
                                        </@td>
                                        <@td true>${item.shelf!}</@td>
                                        <@td true>${item.remain!}${item.units!}</@td>
                                        <@td true>${item.count!}${item.units!}</@td>
                                    </tr>
                                </#list>
                            </@tbody>
                        </@table>
                        <@panelPageFooter action="/admin/inventory/transaction/detail" />
                    </@panel>
                </@panelBody>
                <@panelFooter>
                    <div class="box-footer">
                        <@shiro.hasPermission name="inventory_goods.edit" >
                            <#if transaction.status! == 'pending'>
                                <a href="javascript:void(0)" onclick="doAccept()" class="btn btn-success">
                                    <i class="icon icon-check "></i>确认出库
                                </a>
                                <a href="javascript:void(0)" onclick="doReject()" class="btn btn-danger" style="margin: 0 20px;">
                                    <i class="icon icon-remove "></i>退回申请
                                </a>
                            </#if>
                        </@shiro.hasPermission>
                        <a href="javascript:void(0)" onclick="history.back()" class="btn btn-default">
                            <i class="icon icon-back "></i>返回
                        </a>
                    </div>
                </@panelFooter>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function doAccept() {
            warningModal("确认已经发放所有物资吗？", function () {
                $.post("/admin/inventory/transaction/affirm.json",
                    {id: ${(param.id?c)!}},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            alertShow("info", "确认发放成功！", 3000);
                            window.location.reload();
                        }
                    },
                    "json"
                );
            });
        }
        function doReject() {
            warningModal("确认要取消该申请么？", function () {
                $.post("/admin/inventory/transaction/cancel.json",
                    {id: ${(param.id?c)!}},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            alertShow("info", "取消申请单成功，所有资产已退回待申请列表！", 3000);
                            window.location.href = "/admin/inventory/transaction";
                        }
                    },
                    "json"
                );
            });
        }
    </script>
</@html>
