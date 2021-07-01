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
            <@crumbItem href="#" name="领用出库" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "领用出库" />
                <@panelBody>
                    <@inlineForm class="margin-b-15" id="filter_form">
                        <@formLabelGroup class="margin-r-15" label="领用人：">
                            <@inputText name="user" value="${(param.user)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 3>#</@th>
                                <@th 15>时间</@th>
                                <@th 10>操作者</@th>
                                <@th 10 true>操作类型</@th>
                                <@th 10 true>操作状态</@th>
                                <@th 7 true>品类数量</@th>
                                <@th 7 true>操作总数</@th>
                                <@th>备注信息</@th>
                                <@th 10 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list transactions! as item>
                                <tr>
                                    <@td>
                                        <a href="/admin/inventory/transaction/detail?id=${(item.id?c)!}">${item.id!}</a>
                                    </@td>
                                    <@td>${(item.addTime?string("yyyy-MM-dd"))!}</@td>
                                    <@td>${item.nickname!}</@td>
                                    <@td true>${(item.operate.getName())!}</@td>
                                    <@td true>${(item.status.getName())!}</@td>
                                    <@td true>${item.kindCount!}种</@td>
                                    <@td true>${item.totalCount!}</@td>
                                    <@td>${item.remark!}</@td>
                                    <@td true>
                                        <a href="/admin/inventory/transaction/detail?id=${(item.id?c)!}" class="btn btn-sm btn-primary">
                                            <i class="icon icon-info"></i>
                                        </a>
                                        <#if (item.userId?c)! == (user.id?c)! && (item.status)! == 'pending'>
                                            <button class="btn btn-sm btn-danger" onclick="doCancel(${(item.id?c)!})">
                                                <i class="icon icon-remove "></i>取消
                                            </button>
                                        </#if>
                                    </@td>
                                </tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/inventory/transaction/affirm" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function doCancel(id) {
            warningModal("确认要取消该申请么？", function () {
                $.post("/admin/inventory/transaction/cancel.json",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            alertShow("info", "取消申请单成功，所有资产已退回待申请列表！", 3000);
                            window.location.reload();
                        }
                    },
                    "json"
                );
            });
        }
    </script>
</@html>
