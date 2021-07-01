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
            <@crumbItem href="#" name="出入库查询" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "出入库记录" />
                <@panelBody>
                    <@inlineForm class="margin-b-15" id="filter_form">
                        <@formLabelGroup class="margin-r-15" label="开始日期：">
                            <@inputDate name="beginDate" value="${(param.beginDate?string('yyyy-MM-dd'))!}" placeholder="开天辟地" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="结束日期：">
                            <@inputDate name="endDate" value="${(param.endDate?string('yyyy-MM-dd'))!}" placeholder="海枯石烂" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="资产条码：">
                            <@inputText name="barcode" value="${(param.barcode)!}"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="关键字：">
                            <@inputText name="keyword" value="${(param.keyword)!}"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="操作类型：">
                            <@inputEnum name="operate" enums=operates! blank="全部操作" value="${(param.operate)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-primary"/>
                        <button class="btn btn-success" type="button" onclick="doExport();">
                            <i class="icon icon-download"></i>导出
                        </button>
                        <script>
                            function doExport() {
                                $("#filter_form").attr("action", "/admin/inventory/transaction/journal/export");
                                $("#filter_form").submit();
                                $("#filter_form").attr("action", "/admin/inventory/transaction/journal");
                            }
                        </script>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 3>#</@th>
                                <@th 12>时间</@th>
                                <@th 20>资产名称</@th>
                                <@th 8>条码</@th>
                                <@th 8>操作者</@th>
                                <@th 10 true>操作类型</@th>
                                <@th 7 true>数量</@th>
                                <@th>备注信息</@th>
                                <@th 10 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list details! as item>
                                <tr>
                                    <@td>
                                        <a href="/admin/inventory/transaction/detail?id=${(item.transactionId?c)!}">${item.transactionId!}</a>
                                    </@td>
                                    <@td>${(item.addTime?string("yyyy-MM-dd HH:mm"))!}</@td>
                                    <@td>
                                        ${item.name!}
                                        <div class="small">${item.spec!}</div>
                                    </@td>
                                    <@td>
                                        ${item.barcode!}
                                        <div class="small">${item.code!}</div>
                                    </@td>
                                    <@td>${item.nickname!}</@td>
                                    <@td true>
                                        ${(item.operate.getName())!}
                                        <div class="small">${(item.status.getName())!}</div>
                                    </@td>
                                    <@td true>${item.count!}</@td>
                                    <@td>${item.remark!}</@td>
                                    <@td true>
                                        <a href="/admin/inventory/transaction/detail?id=${(item.transactionId?c)!}" class="btn btn-sm btn-primary">
                                            <i class="icon icon-info"></i>
                                        </a>
                                    </@td>
                                </tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/inventory/transaction/journal" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>
</@html>
