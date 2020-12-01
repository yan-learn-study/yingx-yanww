<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
    $(function () {
        $('#feedbackTable').jqGrid({
            styleUI: "Bootstrap",
            url: "${path}/feedback/queryAllFeedback",
            datatype: "json",
            mtype: 'post',
            colNames: ["Id", "反馈标题", "反馈内容", "反馈时间", "反馈用户"],
            beforeRequest: function () {
                $("thead th").css("text-align", "center").css("font-weight", "bold");
            },
            colModel: [
                {name: 'id', align: 'center', resizable: false, sortable: false, editable: false},
                {name: 'title', align: 'center', resizable: false, sortable: false, editable: false},
                {name: 'content', align: 'center', resizable: false, sortable: false, editable: false},
                {name: 'createDate', resizable: false, align: 'center', sortable: false, editable: false},
                {name: 'userId', align: 'center', sortable: false, resizable: false, editable: false}
            ],
            autowidth: true,
            height: "auto",
            pager: "#feedbackTablePage",
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
    <div class="panel panel-primary">
        <div class="panel-heading" style="height: 60px"><h3 style="margin-top: 10px">反馈信息</h3></div>
        <div class="panel-body" style="padding: 0px 0px;padding-bottom: 0px">
            <div class="col-sm-12" style="padding-left: 0px;padding-right: 0px">
                <table id="feedbackTable"></table>
                <div id="feedbackTablePage"></div>
            </div>
        </div>
    </div>
</div>