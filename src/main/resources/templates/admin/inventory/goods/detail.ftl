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
            pre {outline: 0px solid #eee; padding: 5px; margin: 5px; }
            .string { color: green; }
            .number { color: darkorange; }
            .boolean { color: blue; }
            .null { color: magenta; }
            .key { color: red; }
            .nopadding {
                padding: 0px!important;
            }
            .padding10 {
                padding: 10px!important;
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
                                    名称：
                                </td>
                                <td>
                                    ${goods.name!}
                                </td>
                                <td>
                                    条码：
                                </td>
                                <td>
                                    ${goods.barcode}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    品牌：
                                </td>
                                <td>
                                    ${goods.tradeMark!}
                                </td>
                                <td>
                                    生产商：
                                </td>
                                <td>
                                    ${goods.manuName!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    当前库存：
                                </td>
                                <td <#if goods.remain?default(0) lte goods.threshold?default(0)>
                                    style="color: #ff0000"
                                        </#if>>
                                    ${goods.remain!}${goods.units}
                                </td>
                                <td>
                                    规格：
                                </td>
                                <td>
                                    ${goods.spec!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    累计入库：
                                </td>
                                <td>
                                    ${goods.inputted!}
                                </td>
                                <td>
                                    累计出库：
                                </td>
                                <td>
                                    ${goods.consumed!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    报警阈值：
                                </td>
                                <td>
                                    ${goods.threshold!}
                                </td>
                                <td>
                                    最优库存：
                                </td>
                                <td>
                                    ${goods.preferred!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    资产编码：
                                </td>
                                <td>
                                    ${goods.code!}
                                </td>
                                <td>
                                    货架号：
                                </td>
                                <td>
                                    ${goods.shelf!}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    详细信息：
                                </td>
                                <td colspan="3">
                                    <pre id="detail_pre">
                                        <#if !detailIsJson>
                                            ${goods.detail!};
                                        </#if>
                                    </pre>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        </table>
                    </@panel>
                    <@panel class="panel-info">
                        <div class="panel-heading">
                            出入库清单
                        </div>
                        <@panelBody class="nopadding">
                            <@inlineForm id="filter_form" class="padding10">
                                <@inputHidden name="id" value="${(param.id?c)!}" />
                                <@formLabelGroup class="margin-r-15" label="开始日期：">
                                    <@inputDate name="beginDate" value="${(param.beginDate?string('yyyy-MM-dd'))!}" placeholder="开天辟地" />
                                </@formLabelGroup>
                                <@formLabelGroup class="margin-r-15" label="结束日期：">
                                    <@inputDate name="endDate" value="${(param.endDate?string('yyyy-MM-dd'))!}" placeholder="海枯石烂" />
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
                                        $("#filter_form").attr("action", "/admin/inventory/goods/detail/export");
                                        $("#filter_form").submit();
                                        $("#filter_form").attr("action", "/admin/inventory/goods/detail");
                                    }
                                </script>
                            </@inlineForm>
                            <@table>
                                <@thead>
                                    <@tr>
                                        <@th 3>#</@th>
                                        <@th 12>时间</@th>
                                        <@th 8>操作者</@th>
                                        <@th 10 true>操作类型</@th>
                                        <@th 10 true>操作状态</@th>
                                        <@th 7 true>数量</@th>
                                        <@th>备注信息</@th>
                                    </@tr>
                                </@thead>
                                <@tbody>
                                    <#list items! as item>
                                        <tr>
                                            <@td>
                                                <a href="/admin/inventory/transaction/detail?id=${(item.transactionId?c)!}">${item.transactionId!}</a>
                                            </@td>
                                            <@td>${(item.addTime?string("yyyy-MM-dd HH:mm"))!}</@td>
                                            <@td>${item.nickname!}</@td>
                                            <@td true>${(item.operate.getName())!}</@td>
                                            <@td true>${(item.status.getName())!}</@td>
                                            <@td true>${item.count!}</@td>
                                            <@td>${item.remark!}</@td>
                                        </tr>
                                    </#list>
                                </@tbody>
                            </@table>
                        </@panelBody>
                        <@panelPageFooter action="/admin/inventory/goods/detail" />
                    </@panel>
                </@panelBody>
                <@panelFooter>
                    <div class="box-footer">
                        <a href="javascript:void(0)" onclick="history.back()" class="btn btn-default">
                            <i class="icon icon-back "></i>返回
                        </a>
                    </div>
                </@panelFooter>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function syntaxHighlight(json) {
            if (typeof json != 'string') {
                json = JSON.stringify(json, undefined, 2);
            }
            json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
            return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
                var cls = 'number';
                if (/^"/.test(match)) {
                    if (/:$/.test(match)) {
                        cls = 'key';
                    } else {
                        cls = 'string';
                    }
                } else if (/true|false/.test(match)) {
                    cls = 'boolean';
                } else if (/null/.test(match)) {
                    cls = 'null';
                }
                return '<span class="' + cls + '">' + match + '</span>';
            });
        }
        <#if detailIsJson>
        $('#detail_pre').html(syntaxHighlight(${goods.detail!}));
        </#if>
    </script>
</@html>
