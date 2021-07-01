<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#include "./actions.ftl">
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
            th {
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
        <@crumbRoot name="资产管理" icon="icon-user">
            <@crumbItem href="#" name="资产管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "资产列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15" id="filter_form">
                        <@formLabelGroup class="margin-r-15" label="名称：">
                            <@inputText name="keyword" value="${(param.keyword)!}"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="条码：">
                            <@inputText name="barcode" value="${(param.barcode)!}"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="分类：">
                            <@inputTree options=categories! name="categoryId" value="${(param.categoryId?c)!}" blank="所有品类"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="">
                            <@inputCheckbox text="库存紧张" name="understock" checked="${(param.understock?string('checked', ''))!}" value="1"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="">
                            <@inputCheckbox text="建议采购" name="preferApply" checked="${(param.preferApply?string('checked', ''))!}" value="1"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-primary"/>
                        <button class="btn btn-success" type="button" onclick="doExport();">
                            <i class="icon icon-download"></i>导出
                        </button>
                        <script>
                            function doExport() {
                                $("#filter_form").attr("action", "/admin/inventory/goods/export");
                                $("#filter_form").submit();
                                $("#filter_form").attr("action", "/admin/inventory/goods");
                            }
                        </script>
                        <@rightAction>
                            <@shiro.hasPermission name="inventory_goods.edit" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAddGoods();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 30>名称/规格</@th>
                                <@th 8>分类/货架号</@th>
                                <@th 10>条码/编码</@th>
                                <@th 5 true>库存</@th>
                                <@th 7 true>库存紧张/最优库存</@th>
                                <@th 7 true>累积出库/累计入库</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list goods! as item>
                                <tr>
                                    <@td class="${(item.threshold gt item.remain)?string('red', '')}">
                                        <div><a href="/admin/inventory/goods/detail?id=${(item.id?c)!}">${(item.name)!}</a></div>
                                        <div class="small">${item.spec}</div>
                                    </@td>
                                    <@td>
                                        ${item.category!}
                                        <div class="small">${item.shelf!}</div>
                                    </@td>
                                    <@td>
                                        <span>${(item.barcode)!}</span>
                                        <a href="#" onclick="showBarcode(${(item.id?c)!}, '${(item.barcode)!}', '${item.spec!}', '${item.name!}', '${item.manuName!}')"><i class="icon icon-barcode"></i></a>
                                        <div class="small">${(item.code)!}</div>
                                    </@td>
                                    <@td center=true class="${(item.threshold gt item.remain)?string('red', '')}">
                                        ${item.remain!}${item.units!}
                                    </@td>
                                    <@td true>${item.threshold!} / ${item.preferred!}</@td>
                                    <@td true>${item.consumed!} / ${item.inputted!}</@td>
                                    <@td true>
                                        <a href="/admin/inventory/goods/detail?id=${(item.id?c)!}" class="btn btn-sm btn-primary ">
                                            <i class="icon icon-info"></i>
                                        </a>
                                        <@shiro.hasPermission name="inventory_goods.edit" >
                                            &nbsp;&nbsp;
                                            <button onclick="doEdit(${item.id})" class="btn btn-sm btn-success ">
                                                <i class="icon fa-pencil "></i>编辑
                                            </button>
                                            &nbsp;&nbsp;
                                            <button class="btn btn-sm btn-danger " onclick="doDelete(${item.id})">
                                                <i class="icon fa-remove "></i>删除
                                            </button>
                                        </@shiro.hasPermission>
                                        <@shiro.hasPermission name="inv_input.add.bad" >
                                            <button onclick="doInput('${item.barcode?if_exists}', '${item.name?if_exists}')" class="btn btn-sm btn-success ">
                                                <i class="icon fa-pencil "></i>入库
                                            </button>
                                        </@shiro.hasPermission>
                                    </@td>
                                </tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/inventory/goods" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function doDelete(id) {
            warningModal("确定要删除该资产吗？", function () {
                $.ajax({
                    url: "/admin/inventory/goods/delete.json?id=" + id,
                    type: 'DELETE',
                    success: function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
                });
            });
        }
    </script>


    <@modal title="资产条码" showId="userAddButton" idPrefix="barcode" cancel="关闭" onOk="doPrint" ok="打印">
        <div class="modal-body">
            <div class="modalForm">
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-6" style="text-align: center">
                        <div type="hidden" id="goods_barcode_id"></div>
                        <div id="goods_name" style="font-size: 16px">商品名称</div>
                        <div id="goods_spec" style="color: #777">商品名称</div>
                        <div id="goods_vendor" style="color: #777">商品名称</div>
                        <img style="clear: both; display: block; margin:auto;" id="barcode_show">
                    </div>
                </div>
            </div>
        </div>
    </@modal>
    <script>
        function showBarcode(id, barcode, spec, name, vendor) {
            $("#barcode_modal").modal("show");
            var image = $("#barcode_show");
            image.attr("src", "/admin/inventory/barcode/barcode?type=EAN13&barcode=" + barcode);
            $("#goods_name").html(name);
            $("#goods_spec").html(spec);
            $("#goods_vendor").html(vendor);
            $("#goods_barcode_id").val(id);
        }
        function doPrint() {
            var id = $("#goods_barcode_id").val();
            $.post("/admin/inventory/goods/print.json",
                {id: id},
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        alertShow("info", "提交打印成功！", 3000);
                    }
                },
                "json"
            );
        }
    </script>


    <@goodsEdit />
</@html>
