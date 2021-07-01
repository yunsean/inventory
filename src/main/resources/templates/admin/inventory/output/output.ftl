<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#include "../goods/actions.ftl">
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
            .autocomplete-dropdown td:hover {
                color: green!important;
                font-weight: bold!important;;
            }
            .autocomplete-dropdown tr:not(:last-child) {
                border-bottom: 1px dashed black;
            }
            .autocomplete-dropdown td {
                cursor: pointer;
                word-break:keep-all;
                white-space:nowrap;
                overflow:hidden;
                text-overflow:ellipsis;
                display:block;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="资产管理" icon="icon-user">
            <@crumbItem href="#" name="资产领用" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "资产领用" />
                <@panelBody>
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <div class="form-inline" action="/inv/output">                            &nbsp; &nbsp;
                            <div class="form-group">
                                <label padding-top:5px;">数量：</label>
                                <input type="text" class="form-control" style="width: 50px; text-align: center;" id="add_count" name="add_count" value="1">
                            </div>                           &nbsp; &nbsp;
                            <div class="form-group">
                                <label padding-top:5px;">名称：</label>
                                <div style="display: inline-block">
                                    <input class="form-control" id="goods-output" type="text" autocomplete="off" style="width: 150px;"/>
                                    <input name="reviewerId" type="hidden" id="goods-value"/>
                                    <div class="autocomplete-dropdown" id="goods-result" style="display: none; background: #eee; position: absolute; border: solid blue 1px; border-radius: 4px; padding: 5px;"></div>
                                </div>
                            </div>                          &nbsp; &nbsp;
                            <div class="form-group">
                                <label padding-top:5px;">条码：</label>
                                <input type="text" class="form-control" style="width: 150px; text-align: center;" id="add_barcode" name="add_barcode" onkeypress="if(event.keyCode==13) {doAdd();return false;}">
                            </div>                     &nbsp; &nbsp;
                            <button class="btn btn-success" onclick="doAdd()">
                                <i class="icon icon-plus"></i>添加
                            </button>                        &nbsp; &nbsp;
                            <a href="#" class="btn btn-primary" onclick="doAddBatch()">
                                <i class="icon icon-plus"></i>批量添加</a>                        &nbsp; &nbsp;
                            <button class="btn btn-danger" onclick="doClean()">
                                <i class="icon icon-file-zip"></i>清空
                            </button>                        &nbsp; &nbsp;
                            <a href="#" class="btn btn-success" onclick="doSubmit()">
                                <i class="icon icon-eye-slash"></i>领用提交
                            </a>
                            <div style="float:right">
                                <div class="form-group">
                                    <label padding-top:5px;">过滤：</label>
                                    <input type="text" class="form-control" id="filter" name="filter" value="">
                                </div>                            &nbsp; &nbsp;
                                <button class="btn btn-check" type="submit" onclick="doFilter()">
                                    <i class="icon icon-check"></i>筛选
                                </button>
                            </div>
                        </div>
                    </div>
                    <iframe id="intent_list" width="100%" src="/admin/inventory/output/list" onload="Javascript:SetCwinHeight()" frameborder="0" src="index.htm"></iframe>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>


    <script>
        function SetCwinHeight() {
            var iframeid = document.getElementById("intent_list"); //iframe id
            if (document.getElementById) {
                if (iframeid && !window.opera) {
                    if (iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight) {
                        iframeid.height = iframeid.contentDocument.body.offsetHeight + 40;
                    } else if (iframeid.Document && iframeid.Document.body.scrollHeight) {
                        iframeid.height = iframeid.Document.body.scrollHeight + 40;
                    }
                }
            }
        }
        $(function () {
            setupAutoCompleteGoods($("#goods-output"), $("#goods-result"), $("#goods-value"), "goods-table", addGoods2);
        });

        function addGoods2(id, name) {
            var count = $("#add_count").val();
            $.post(
                "/admin/inventory/output/add/id.json",
                {id: id, count: count},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        document.getElementById('intent_list').contentWindow.location.reload(true);
                    }
                }
            );
        }
    </script>

    <script>
        function doFilter() {
            var filter = $("#filter").val();
            document.getElementById('intent_list').src = "/admin/inventory/output/list?keyword=" + filter;
        }
        function doAdd() {
            var count = $("#add_count").val();
            var barcode = $("#add_barcode").val();
            if (barcode == "")return;
            $("#add_barcode").val("");
            $.post(
                "/admin/inventory/output/add/code.json",
                {barcode: barcode, count: count},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        document.getElementById('intent_list').contentWindow.location.reload(true);
                    }
                }
            );
        }
    </script>


    <div class="modal fade" id="batch_add_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" style="width:50%">
            <form class="form-horizontal" id="batch_add_modal">
                <div class="modal-templates.content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="myModalLabel">
                            批量添加
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div class="modalForm">
                            <#if scanner?exists && scanner.type?exists && scanner.read?exists>
                                <div class="form-group">
                                    <label class="col-sm-3  control-label">读取存储：</label>
                                    <div class="col-sm-8">
                                        <img style="clear: both; display: block; margin:auto;" id="submit_scanner_read">
                                    </div>
                                </div>
                            </#if>
                            <div class="form-group">
                                <label class="col-sm-3  control-label">条码列表（每行一个条码）：</label>
                                <div class="col-sm-8">
                                <textarea class="form-control" id="batch_barcodes"
                                          style='overflow-y: hidden;height:300px'></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="#" class="btn btn-danger" data-dismiss="modal">
                            <i class="icon icon-remove"></i>取消
                        </a>
                        <a type="summit" class="btn btn-info" onclick="batchAdd()">
                            <i class="fa fa-fw  fa-save"></i>添加
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <script>
        function doAddBatch() {
            $("#batch_add_modal").modal("show");
            var image = $("#submit_scanner_read");
            image.attr("src", "/admin/inventory/barcode/barcode?barcode=" + encodeURIComponent('${(scanner.read)!}') + "&type=${(scanner.type)!}");
        }
        function batchAdd() {
            var barcodes = $("#batch_barcodes").val();
            $.post(
                "/admin/inventory/output/add/batch.json",
                {barcodes: barcodes},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        <#if scanner?exists && scanner.type?exists && scanner.clean?exists>
                        $("#clean_modal").modal("show");
                        var image = $("#submit_scanner_clean");
                        image.attr("src", "/api/inv/barcode/barcode?barcode=" + encodeURIComponent('${scanner.clean}') + "&type=${scanner.type}");
                        </#if>
                        $("#batch_add_modal").modal("hide");
                        $("#batch_barcodes").val("");
                        document.getElementById('intent_list').contentWindow.location.reload(true);
                    }
                }
            );
        }
        function doClean() {
            warningModal("确定要清空待领用清单吗?", function () {
                $.ajax({
                    url: "/admin/inventory/output/clean.json",
                    type: 'DELETE',
                    success: function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            document.getElementById('intent_list').contentWindow.location.reload(true);
                        }
                    }
                });
            });
        }
    </script>


    <@modal title="领用提交" idPrefix="submit" onOk="submit" width=50>
        <@formTextArea name="remark" label="备注信息：" id="submit_remark"/>
    </@modal>
    <script>
        $(function () {
            setTimeout(function () {
                $("#add_barcode").focus().select();
            }, 500)
        })
        function doSubmit() {
            $("#submit_modal").modal("show");
        }
        function submit() {
            var remark = $("#submit_remark").val();
            $.ajax({
                type: "post",
                url: "/admin/inventory/output/submit.json",
                data: JSON.stringify({remark: remark}),
                contentType: "application/json;charset=utf-8",
                dataType: "json",
                success: function(data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#submit_modal").modal("hide");
                        document.getElementById('intent_list').contentWindow.location.reload(true);
                    }
                }
            });
        }
    </script>

    <@goodsEdit />
    <@goodsChoice />
</@html>
