<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true>
        <style>
            .checked {
                background-color: #eee;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="资产管理" icon="icon-user">
            <@crumbItem href="#" name="分类管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "分类管理" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@shiro.hasPermission name="inventory_category.edit" >
                            <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd(0);" />
                        </@shiro.hasPermission>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 25>分类名称</@th>
                                <@th 50>分类描述</@th>
                                <@th 25 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <@m1_columns2 categories!/>
                        </@tbody>
                    </@table>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        $(function () {
            $("td").mouseover(function(){
                $(this).css("cursor","pointer");
                $(this).parent().addClass("checked");
            });
            $("td").mouseout(function(){
                $(this).css("cursor","auto");
                $(this).parent().removeClass("checked");
            });
        });
        function rowClick(id, obj) {
            var list = document.getElementsByName(id);
            var objImg = $(obj).find("img").attr("src");
            if (objImg == undefined) return;
            if (objImg.indexOf("expansion.png") > 0) {
                $(obj).find("img").attr("src", "/admin/tree/shrink.png");
                for (var i = 0; i < list.length; i++) {
                    list[i].style.display = "";
                }
            } else {
                $(obj).find("img").attr("src", "/admin/tree/expansion.png");
                for (var i = 0; i < list.length; i++) {
                    list[i].style.display = "none";
                    var img = $(list[i]).find("img").attr("src");
                    if (img == undefined) continue;
                    if (img.indexOf("shrink.png") > 0) {
                        $(list[i]).children().eq(0).click();
                    }
                }
            }
        }
        function doDelete(id) {
            warningModal("确定要删除该分类吗？", function () {
                $.ajax({
                    url: "/admin/inventory/category/delete.json?id=" + id,
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

    <#macro m1_column2 category level index>
        <tr name="${(category.parentId?c)!}" id="${(category.id?c)!}">
            <td onclick="rowClick(${(category.id?c)!},this)" class="${(category.id?c)!}" style=" padding-left: ${level * 20 + 10}px;height: 40px;">
                <#if category.children??>
                    <img src="/admin/tree/shrink.png" width="18px;" height="9px;">${(category.name)!}
                <#else >
                    &nbsp;&nbsp;&nbsp;&nbsp;${(category.name)!}
                </#if>
            </td>
            <td>${(category.remark)!}</td>
            <td style="text-align: center">
                <@shiro.hasPermission name="admin_branch.add" >
                    <a href="javascript:void(0)" onclick="doAdd(${(category.id?c)!})" class="btn btn-sm btn-info">
                        <i class="icon icon-plus"></i>添加
                    </a>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="admin_branch.update" >
                    <a href="javascript:void(0)" onclick="doEdit(${(category.id?c)!})" class="btn btn-sm btn-primary">
                        <i class="icon icon-edit"></i>编辑
                    </a>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="admin_branch.del" >
                    <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(category.id?c)!})">
                        <i class="icon icon-remove"></i>删除
                    </a>
                </@shiro.hasPermission>
            </td>
        </tr>
    </#macro>
    <#macro m1_columns2 columns level=0>
        <#if columns??>
            <#list columns as column>
                <@m1_column2 column level column_index/>
                <#local level1 = level + 1/>
                <#if column.children??>
                    <@m1_columns2 column.children level1/>
                </#if>
            </#list>
        </#if>
    </#macro>

    <#macro m1_column column level index>
        <option value="${(column.id?c)!}">
            <#list 0..level as x><#if x < level>┃&nbsp;&nbsp;<#else>┠</#if></#list>${(column.name)!}
        </option>
    </#macro>
    <#macro m1_columns columns level index>
        <@m1_column columns level index/>
        <#local level1 = level + 1/>
        <#if columns.children??>
            <#list columns.children as sub>
                <@m1_columns sub level1 sub_index/>
            </#list>
        </#if>
    </#macro>

    <@modal title="分类编辑" showId="userAddButton" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="分类名称：" />
        <div class="form-group">
            <label class="col-sm-3 control-label">上级分类：</label>
            <div class="col-sm-8">
                <select class="form-control" name="parentId" id="parentId">
                    <option value="0">顶级分类</option>
                    <#list categories! as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
        <@formTextArea name="remark" label="描述信息：" />
    </@modal>

    <script>
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/inventory/category/add.json" : "/admin/inventory/category/update.json",
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
        function doAdd(parentId) {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_form select[name='parentId']").val(parentId);
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                "/admin/inventory/category/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        console.log(data)
                        $("#add_form input[name='id']").val(id);
                        $("#add_form input[name='name']").val(data.result.name);
                        $("#add_form select[name='parentId']").val(data.result.parentId);
                        $("#add_form textarea[name='remark']").val(data.result.remark);
                        if (data.result.roleIds) {
                            data.result.roleIds.forEach(function (roleId) {
                                $("#add_form input[name='roleIds'][value=" + roleId + "]").prop("checked", true);
                            });
                        }
                        $("#add_modal").modal("show");
                    }
                }
            );
        }
    </script>
</@html>
