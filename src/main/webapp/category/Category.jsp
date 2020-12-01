<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
    $(function () {
        function addSubFrid(subgridId, rowId) {
            var subgridTableId = subgridId + "Table";
            let pagerId = "page" + subgridTableId;
            $("#" + subgridId).html("<table id='" + subgridTableId + "'/><div id='" + pagerId + "'/>")
            jQuery("#" + subgridTableId).jqGrid(
                {
                    url: '${path}/category/queryAllTwoLevel?id=' + rowId,
                    datatype: "json",
                    colNames: ['Id', '二级分类名', '级别', '父类别Id'],
                    beforeRequest: function () {
                        $("thead th").css("text-align", "center").css("font-weight", "bold");
                    },
                    colModel: [
                        {name: "id", index: "id", align: "center"},
                        {name: "name", index: "name", align: "center", editable: true},
                        {name: "level", index: "level", align: "center"},
                        {name: "parentId", index: "parentId", align: "center"},
                    ],
                    styleUI: "Bootstrap",
                    autowidth: true,
                    height: "auto",
                    rowList: [5, 10, 20, 30],
                    rowNum: 5,
                    pager: "#" + pagerId,
                    height: 'auto',
                    editurl: '${path}/category/editCategory?parentId=' + rowId
                });
            $("#" + subgridTableId).jqGrid('navGrid', "#" + pagerId,
                {add: true, edit: true, del: true, search: false},   //参数2:开启工具栏编辑按钮
                {
                    closeAfterEdit: true,
                    afterSubmit: function (response) {
                        afterSubimt(response);
                        return "true";
                    }
                },//编辑面板的配置
                {
                    closeAfterAdd: true,
                    afterSubmit: function (response) {
                        afterSubimt(response);
                        return "true";
                    }
                },//添加面板的配置
                {
                    closeAfterDelete: true,
                    afterSubmit: function (response) {
                        afterSubimt(response);
                        return "true";
                    }
                });
        }

        function afterSubimt(response) {
            console.log(response);
            if (response.responseJSON.status == 201) {
                $('#editCategoryAlert').attr('class', 'alert alert-danger alert-dismissible');
            } else {
                $('#editCategoryAlert').attr('class', 'alert alert-success alert-dismissible');
            }
            $('#editCategoryAlert').show();
            $('#editCategoryMessage').html(response.responseJSON.message);
            $("#categoryTable").trigger("reloadGrid");
            setInterval(function () {
                $('#editCategoryAlert').hide();
            }, 3000);
        }

        $('#categoryTable').jqGrid({
            styleUI: "Bootstrap",
            url: "${path}/category/queryAllOneLevel",
            datatype: "json",
            mtype: 'post',
            colNames: ["Id", "一级分类名", "级别"],
            beforeRequest: function () {
                $("thead th").css("text-align", "center").css("font-weight", "bold");
            },
            colModel: [
                {name: 'id', align: 'center', resizable: false, sortable: false},
                {label: '一级类别', name: 'name', align: 'center', resizable: false, sortable: false, editable: true},
                {name: 'level', align: 'center', resizable: false, sortable: false},
            ],
            autowidth: true,
            height: "auto",
            pager: "#categoryTablePage",
            page: 1,
            rowList: [5, 10, 20, 30],
            rowNum: 5,
            viewrecords: true,
            hiddengrid: false,
            hidegrid: true,
            editurl: '${path}/category/editCategory',
            subGrid: true,
            subGridRowExpanded: function (subgrid_id, row_id) {
                addSubFrid(subgrid_id, row_id);
            }
        }).jqGrid('navGrid',
            '#categoryTablePage',//参数1: 分页工具栏id
            {add: true, edit: true, del: true, search: false},   //参数2:开启工具栏编辑按钮
            {
                closeAfterEdit: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    afterSubimt(response);
                    return "true";
                }
            },//编辑面板的配置
            {
                closeAfterAdd: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    afterSubimt(response);
                    return "true";
                }
            },//添加面板的配置
            {
                closeAfterDelete: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    afterSubimt(response);
                    return "true";
                }
            })//删除的配置
    });
</script>
<!--分类展示-->
<div class="col-sm-10">
    <div class="panel panel-success">
        <div class="panel-heading" style="height: 60px"><h3 style="margin-top: 10px">类别管理</h3></div>
        <div class="panel-body" style="padding: 10px 0px;padding-bottom: 0px">
            <div class="col-sm-12" style="margin: 0">
                <div class="col-sm-6 col-sm-offset-3">
                    <div class="alert alert-danger alert-dismissible" role="alert" id="editCategoryAlert"
                         style="display: none">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <strong id="editCategoryMessage"></strong>
                    </div>
                </div>
                <div class="col-sm-3"></div>
            </div>
            <div class="col-sm-12" style="padding: 10px 0px;padding-bottom: 0px;margin: 0">
                <table id="categoryTable"></table>
                <div id="categoryTablePage"></div>
            </div>
        </div>
    </div>
</div>
