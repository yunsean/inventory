<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head>
    <style>
        td {
            vertical-align:middle;
        }
        .small {
            font-size: 85%;
            color: #999;
        }
    </style>
    </@head>
    <div class="container-fluid">
        <table class="table table-bordered table-striped">
            <thead>
            <@tr>
                <@th 22>名称</@th>
                <@th 20>生产厂家</@th>
                <@th 12 true>条码</@th>
                <@th 10>分类</@th>
                <@th 7 true>库存</@th>
                <@th 15 true>入库数量</@th>
                <@th 15 true>操作</@th>
            </@tr>
            </thead>
            <tbody>
                <#list records as item>
                <tr <#if item.goodsId?default(0) == 0>bgcolor="#fffff0"</#if>>
                    <@td>
                        ${item.name!}
                        <br><div class="small">${item.spec!}</div>
                    </@td>
                    <@td>${item.manuName!}</@td>
                    <@td true>
                        ${item.barcode!}
                        <div class="small">${item.code!}</div>
                    </@td>
                    <@td>${item.category!}</@td>
                    <@td true>${item.remain!}${item.units!}</@td>
                    <@td true>
                        <a onclick="addInput('${(item.goodsId?c)!}', -1)">
                            <img src="/admin/images/asset_sub.png" style="width: 20px; height: 20px">
                        </a>                        &nbsp;&nbsp;
                        <input type="text" style="width: 60px; text-align: center" onblur="setNumber('${(item.goodsId?c)!}', this);" onkeypress="if(event.keyCode==13) {setNumber('${(item.goodsId?c)!}', this);return false;}" barcode="${item.barcode!}" value="${(item.count?c)!}">
                        <a onclick="addInput('${(item.goodsId?c)!}', 1)">
                            <img src="/admin/images/asset_add.png" style="width: 20px; height: 20px">
                        </a>
                    </@td>
                    <@td true>
                        <button class="btn btn-sm btn-danger " onclick="doDelete(${(item.goodsId?c)!})">
                            <i class="icon icon-remove "></i>删除
                        </button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <button class="btn btn-sm btn-success " onclick="doPrint(${(item.goodsId?c)!})">
                            <i class="icon icon-print "></i>打印条码
                        </button>
                    </@td>
                </tr>
                </#list>
            </tbody>
        </table>
        <@panelPageFooter action="/admin/inventory/input/list" />
    </div>

    <script>
        function addInput(id, count) {
            $.post(
                    "/admin/inventory/input/add/id.json",
                    {id: id, count: count},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload()
                        }
                    }
            );
        }
        function setNumber(id, input) {
            var count = $(input).val();
            $.post(
                    "/admin/inventory/input/add/set.json",
                    {id: id, count: count},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            $("#deptAddModal").modal("hide");
                            window.location.reload();
                        }
                    }
            );
        }
        function doDelete(id) {
            warningModal("确定要删除该分类吗?", function(){
                $.ajax({
                    url: "/admin/inventory/input/delete.json?id=" + id,
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
        function doPrint(batchId) {
            alertShow("info", "暂不支持打印！", 3000);
        }
    </script>
</@html>