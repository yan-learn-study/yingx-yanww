<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
    $(function () {
        $('#logTable').jqGrid({
            styleUI: "Bootstrap",
            url: "${path}/log/queryAllLog",
            datatype: "json",
            mtype: 'post',
            colNames: ["Id", "操作人", "操作时间", "执行操作", "状态"],
            beforeRequest: function () {
                $("thead th").css("text-align", "center").css("font-weight", "bold");
            },
            colModel: [
                {name: 'id', align: 'center', resizable: false, sortable: false, editable: false},
                {name: 'username', align: 'center', resizable: false, sortable: false, editable: false},
                {name: 'optionTime', align: 'center', resizable: false, sortable: false, editable: false},
                {name: 'options', resizable: false, align: 'center', sortable: false, editable: false},
                {name: 'status', align: 'center', sortable: false, resizable: false, editable: false}
            ],
            autowidth: true,
            height: "auto",
            pager: "#logTablePage",
            page: 1,
            rowList: [5, 10, 20, 30],
            rowNum: 20,
            viewrecords: true,
            hiddengrid: false,
            hidegrid: true
        });
    });
</script>
<!--用户展示-->
<div class="col-sm-10">
    <div class="panel panel-danger">
        <div class="panel-heading" style="height: 60px"><h3 style="margin-top: 10px">日志信息</h3></div>
        <div class="panel-body" style="padding: 0px 0px;padding-bottom: 0px">
            <div class="col-sm-12" style="padding-left: 0px;padding-right: 0px">
                <table id="logTable"></table>
                <div id="logTablePage"></div>
            </div>
        </div>
    </div>
</div>