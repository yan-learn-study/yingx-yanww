<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
    $(function () {
        $('#videoTable').jqGrid({
            styleUI: "Bootstrap",
            url: "${path}/video/queryVideoByPage",
            datatype: "json",
            mtype: 'post',
            colNames: ["Id", "视频名称", "视频封面", "视频内容", "上传时间", "描述", "所属类别Id", "上传用户Id"],
            beforeRequest: function () {
                $("thead th").css("text-align", "center").css("font-weight", "bold");
            },
            colModel: [
                {name: 'id', align: 'center', resizable: false, sortable: false},
                {name: 'title', align: 'center', resizable: false, sortable: false, editable: true},
                {
                    name: 'coverPath',
                    align: 'center',
                    resizable: false,
                    sortable: false,
                    editable: false,
                    width: 350,
                    hidden: true,
                    formatter: function (value) {
                        if (value != null) return "<img src='" + value + "' style='width: 200px;height: 120px'/>";
                        return "";
                    }
                },
                {
                    name: 'videoPath',
                    resizable: false,
                    align: 'center',
                    sortable: false,
                    editable: true,
                    edittype: 'file',
                    width: 320,
                    formatter: function (value, option, rows) {
                        if (value != null) return "<video src='" + value + "' style='width: 200px;height: 120px' controls='controls' poster='" + rows.coverPath + "' ></video>";
                        return "";
                    }
                },
                {name: 'uploadTime', align: 'center', sortable: false, resizable: false},
                {name: 'intro', align: 'center', resizable: false, sortable: false, editable: true},
                {
                    name: 'categoryId',
                    sortable: false,
                    resizable: false,
                    align: 'center',
                    editable: true,
                    edittype: 'select',
                    editoptions: {dataUrl: '${path}/category/queryAllCategory'}
                },
                {name: 'userId', sortable: false, align: 'center', resizable: false}
            ],
            autowidth: true,
            height: "auto",
            pager: "#videoTablePage",
            page: 1,
            rowList: [5, 10, 20, 30],
            rowNum: 5,
            viewrecords: true,
            hiddengrid: false,
            hidegrid: true,
            editurl: '${path}/video/editVideo'
        }).navGrid(
            '#videoTablePage',//参数1: 分页工具栏id
            {add: true, edit: true, del: true, search: true},
            {
                closeAfterEdit: true,
                afterSubmit: function (response) {
                    let id = response.responseJSON.rows.id;
                    $.ajaxFileUpload({
                        url: "${path}/video/uploadVideo",
                        type: "post",
                        data: {"id": id},
                        fileElementId: "videoPath", //文件选择框的id属性，即<input type="file">的id
                        success: function () {
                            //上传成功 所作的事情
                            //刷新页面
                            $("#videoTable").trigger("reloadGrid");
                        }
                    })
                    return "true";
                }
            },//编辑面板的配置
            {
                closeAfterAdd: true,
                afterSubmit: function (response) {
                    console.log(response);
                    let id = response.responseJSON.rows.id;
                    $.ajaxFileUpload({
                        url: "${path}/video/uploadVideo",
                        type: "post",
                        data: {"id": id},
                        fileElementId: "videoPath", //文件选择框的id属性，即<input type="file">的id
                        success: function () {
                            //上传成功 所作的事情
                            //刷新页面
                            $("#videoTable").trigger("reloadGrid");
                        }
                    })
                    return "true";
                }
            },//添加面板的配置
            {
                closeAfterDelete: true
            });//删除的配置);
    });
</script>
<!--用户展示-->
<div class="col-sm-10">
    <div class="panel panel-info">
        <div class="panel-heading" style="height: 60px"><h3 style="margin-top: 10px">用户信息</h3></div>
        <div class="panel-body" style="padding: 0px 0px;padding-bottom: 0px">
            <div class="col-sm-12" style="padding-left: 0px;">
                <table id="videoTable"></table>
                <div id="videoTablePage"></div>
            </div>
        </div>
    </div>
</div>