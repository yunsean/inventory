<#macro goodsEdit>
    <@modal title="资产编辑" showId="userAddButton" onOk="doSave" width=70>
        <@inputHidden name="id" id="edit_id"/>
        <div class="modalForm">
            <div class="form-group">
                <label class="col-sm-3  control-label">资产条码：</label>
                <div class="col-sm-3">
                    <input type="text" name="barcode" class="form-control" id="new_barcode" onkeypress="if(event.keyCode==13) {queryBarcode();return false;}">
                </div>
                <img src="/admin/images/waiting.gif" id="new_waiting" style="width: 32px; height: 32px; display: none;">
                <a class="btn btn-info" onclick="queryBarcode();" id="new_query">查询</a>                &nbsp;&nbsp;&nbsp;
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">资产名称：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control " name="name" oninput="loadSpell(this);">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-2 control-label">资产全拼：</label>
            <div class="col-sm-3">
                <@inputText name="spell"/>
            </div>
            <label class="col-sm-2 control-label">资产首字母：</label>
            <div class="col-sm-3">
                <@inputText name="initial"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-2 control-label">商品品牌：</label>
            <div class="col-sm-3">
                <@inputText name="tradeMark"/>
            </div>
            <label class="col-sm-2 control-label">规格参数：</label>
            <div class="col-sm-3">
                <@inputText name="spec"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-2 control-label">所属品类：</label>
            <div class="col-sm-3">
                <@inputTree name="categoryId" options=categories! />
            </div>
            <label class="col-sm-2 control-label">生产厂家：</label>
            <div class="col-sm-3">
                <@inputText name="manuName"/>
            </div>
        </div>
        <@formImage name="image" label="商品图标：" editable=true divId="new_image"/>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-2 control-label">计量单位：</label>
            <div class="col-sm-3">
                <@inputText name="units"/>
            </div>
            <label class="col-sm-2 control-label">当前库存：</label>
            <div class="col-sm-3">
                <@inputText name="remain" value="0"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-2 control-label">告警数量：</label>
            <div class="col-sm-3">
                <@inputText name="threshold" value="0"/>
            </div>
            <label class="col-sm-2 control-label">最优库存：</label>
            <div class="col-sm-3">
                <@inputText name="preferred" value="0"/>
            </div>
        </div>
        <div class="form-group" id="add-stock-adjust">
            <label class="col-sm-offset-1 col-sm-2 control-label">总消耗量：</label>
            <div class="col-sm-3">
                <@inputText name="consumed" value="0"/>
            </div>
            <label class="col-sm-2 control-label">总入库量：</label>
            <div class="col-sm-3">
                <@inputText name="inputted" value="0"/>
            </div>
        </div>
        <div class="form-group" id="add-stock-adjust">
            <label class="col-sm-offset-1 col-sm-2 control-label">资产编码：</label>
            <div class="col-sm-3">
                <@inputText name="code"/>
            </div>
            <label class="col-sm-2 control-label">货架号：</label>
            <div class="col-sm-3">
                <@inputText name="shelf"/>
            </div>
        </div>
        <@formTextArea name="detail" label="资产详情：" />
    </@modal>

    <script>
        function loadSpell(elem) {
            var name = $(elem).val();
            $.get(
                "/admin/inventory/goods/spell.json?name=" + name,
                function (data) {
                    if (parseInt(data.code) >= 0) {
                        $("#add_form input[name='spell']").val(data.result.spell);
                        $("#add_form input[name='initial']").val(data.result.initial);
                    }
                }
            );
        }
        function queryBarcode() {
            var barcode = $("#new_barcode").val();
            if ($("#new_query").attr("disabled") == 'disabled') {
                alertShow("info", "正在查询，请稍候！", 1000);
                return;
            }
            $("#new_query").attr("disabled", true);
            $("#new_waiting").css('display', 'inline');
            $.get(
                "/admin/inventory/goods/query.json?barcode=" + barcode,
                function (data) {
                    $("#new_query").attr("disabled", false);
                    $("#new_waiting").css('display', 'none');
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        var status = data.result.status;
                        if (status == 'NOTHING') {
                            $("#add_form input[name='name']").val(data.result.infos.goodsName);
                            $("#add_form input[name='tradeMark']").val(data.result.infos.trademark);
                            $("#add_form input[name='manuName']").val(data.result.infos.manuName);
                            $("#add_form input[name='spec']").val(data.result.infos.spec);
                            $("#add_form textarea[name='detail']").val(JSON.stringify(data.result.infos, null, 2));
                            if (data.result.infos.img) {
                                $("#new_image_value").val(data.result.infos.img);
                                $("#new_image_image").attr("src", data.result.infos.img);
                            }
                            var units = "个"
                            if (data.result.infos.spec) {
                                var pos = data.result.infos.spec.lastIndexOf('/');
                                if (pos > 0) {
                                    units = data.result.infos.spec.substring(pos + 1);
                                }
                            }
                            $("#add_form input[name='units']").val(units);
                            $.get(
                                "/admin/inventory/goods/spell.json?name=" + data.result.infos.goodsName,
                                function (data) {
                                    if (parseInt(data.code) >= 0) {
                                        $("#add_form input[name='spell']").val(data.result.spell);
                                        $("#add_form input[name='initial']").val(data.result.initial);
                                    }
                                }
                            );
                        } else if (status == 'UNKNOWN') {
                            alertShow("warning", "未查询到该条码信息，请手动输入资产详情！", 3000);
                        } else {
                            $("#new_barcode").val("");
                            alertShow("warning", "该资产已经入库，请勿重复添加！", 3000);
                        }
                    }
                }
            );
        }
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/inventory/goods/add.json" : "/admin/inventory/goods/update.json",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        $("#add_modal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
            );
        }
        function doAddGoods() {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add-stock-adjust").css('display', 'none');
            $("#new_query").css('display', 'inline-block');
            $("#add_modal").modal("show");
            setTimeout(function () {
                $("#new_barcode").focus().select();
            }, 500)
            setTimeout(function () {
                $("#new_barcode").focus().select();
            }, 1000)
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $("#add-stock-adjust").css('display', 'block');
            $("#new_query").css('display', 'none');
            $.get(
                "/admin/inventory/goods/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='id']").val(data.result.id);
                        $("#add_form input[name='barcode']").val(data.result.barcode);
                        $("#add_form input[name='code']").val(data.result.code);
                        $("#add_form input[name='name']").val(data.result.name);
                        $("#add_form input[name='spell']").val(data.result.spell);
                        $("#add_form input[name='initial']").val(data.result.initial);
                        $("#add_form input[name='tradeMark']").val(data.result.tradeMark);
                        $("#add_form input[name='manuName']").val(data.result.manuName);
                        $("#add_form input[name='spec']").val(data.result.spec);
                        $("#add_form textarea[name='detail']").val(JSON.stringify(data.result.detail, null, 2));
                        if (data.result.image) {
                            $("#new_image_value").val(data.result.image);
                            $("#new_image_image").attr("src", data.result.image);
                        }
                        $("#add_form select[name='categoryId']").val(data.result.categoryId);
                        $("#add_form input[name='units']").val(data.result.units);
                        $("#add_form input[name='shelf']").val(data.result.shelf);
                        $("#add_form input[name='remain']").val(data.result.remain);
                        $("#add_form input[name='threshold']").val(data.result.threshold);
                        $("#add_form input[name='preferred']").val(data.result.preferred);
                        $("#add_form input[name='consumed']").val(data.result.consumed);
                        $("#add_form input[name='inputted']").val(data.result.inputted);
                        $("#add_modal").modal("show");
                    }
                }
            );
        }
    </script>
</#macro>

<#macro goodsChoice>
    <script>
        function setupAutoCompleteGoods(input, result, value, tableId, onOk = null) {
            input.keydown(function (event) {
                var k = window.event ? event.keyCode : event.which;
                if (k == 13) return false;
                else return true;
            });
            var onkeyup = function (event) {
                value.val(null)
                var left = input.position().left;
                var top = input.position().top + input.outerHeight();
                result.css("left", left + "px");
                result.css("top", top + "px");
                result.css("width", "300px");
                var key = window.event ? event.keyCode : event.which;
                if (/*input.val() != "" && */key != 38 && key != 40 && key != 13) {
                    $.get('/admin/inventory/goods/filter.json?keyword=' + input.val(), function (data) {
                        if (data.result && data.result.length > 0) {
                            var layer = "";
                            layer = "<table id='" + tableId + "' style='table-layout:fixed;'>";
                            $.each(data.result, function (idx, item) {
                                layer += "<tr style='width: 100%;' class='line' data-id='" + item.id + "' data-name='" + item.name + "'>";
                                layer += "<td style='width: 290px'>" + item.name;
                                if (item.category) layer += "<small style='color: gray; margin-left: 10px'>" + item.category + "</small>";
                                if (item.spec) layer += "<br><small style='color: gray'>" + item.spec + "</small>";
                                layer += "</td></tr>";
                            });
                            layer += "</table>";
                            result.empty();
                            result.append(layer);
                            $(".line:first").addClass("hover");
                            result.css("display", "");
                            $(".line").hover(function () {
                                $(".line").removeClass("hover");
                                $(this).addClass("hover");
                            }, function () {
                                $(this).removeClass("hover");
                            });
                            $(".line").click(function () {
                                if (onOk) {
                                    onOk($(this).attr('data-id'), $(this).attr('data-name'));
                                    value.val("");
                                    input.val("");
                                } else {
                                    value.val($(this).attr('data-id'));
                                    input.val($(this).attr('data-name'));
                                }
                                result.css("display", "none");
                            });
                        } else {
                            result.empty();
                            result.css("display", "none");
                        }
                    });
                } else if (key == 38) {
                    $('#' + tableId + ' tr.hover').prev().addClass("hover");
                    $('#' + tableId + ' tr.hover').next().removeClass("hover");
                    value.val($('#' + tableId + ' tr.hover').attr('data-id'));
                    input.val($('#' + tableId + ' tr.hover').attr('data-name'));
                } else if (key == 40) {
                    $('#' + tableId + ' tr.hover').next().addClass("hover");
                    $('#' + tableId + ' tr.hover').prev().removeClass("hover");
                    value.val($('#' + tableId + ' tr.hover').attr('data-id'));
                    input.val($('#' + tableId + ' tr.hover').attr('data-name'));
                } else if (key == 13) {
                    if (onOk) {
                        onOk($('#' + tableId + ' tr.hover').attr('data-id'), $('#' + tableId + ' tr.hover').attr('data-name'));
                    } else {
                        value.val($('#' + tableId + ' tr.hover').attr('data-id'));
                        input.val($('#' + tableId + ' tr.hover').attr('data-name'));
                    }
                    result.empty();
                    result.css("display", "none");
                } else {
                    result.empty();
                    result.css("display", "none");
                }
            };
            input.blur(function() {
                if (!result.is(":focus")) {
                    setTimeout(function () {
                        result.css("display", "none");
                    }, 500);
                }
            });
            input.click(function () {
                onkeyup(event);
            });
            input.focus(function (event) {
                onkeyup(event);
            });
            input.keyup(function (event) {
                onkeyup(event);
            });
        }
    </script>
</#macro>