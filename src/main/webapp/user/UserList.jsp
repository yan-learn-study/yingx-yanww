<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">

    function modifyUserStatus(id, status) {
        var b = confirm("确定修改吗？");
        if (b) {
            $.ajax({
                url: '${path}/user/modifyUserStatus',
                data: {'id': id, 'status': status},
                type: 'post',
                datatype: 'json',
                success: function (data) {
                    if (data.status == "200") {
                        //alert(data.message);
                        $('#userTable').jqGrid('clearGridData');
                        $('#userTable').trigger('reloadGrid');
                    } else alert(data.message);
                }
            });
        }
    }

    $(function () {
        $('#userTable').jqGrid({
            styleUI: "Bootstrap",
            url: "${path}/user/queryUserByPage",
            datatype: "json",
            mtype: 'post',

            colNames: ["Id", "用户名", "头像", "性别", "所在地", "简介", "手机号", "状态", "注册时间"],
            beforeRequest: function () {
                $("thead th").css("text-align", "center").css("font-weight", "bold");
            },
            colModel: [
                {name: 'id', align: 'center', resizable: false, sortable: false},
                {name: 'username', align: 'center', sortable: false, editable: true},
                {
                    name: 'headShow', align: 'center', sortable: false, editable: true, edittype: 'file',
                    formatter: function (value) {
                        return "<img src='" + value + "'style='width:30px;' class='img-circle'/>"
                    }
                },
                {
                    name: 'sex',
                    align: 'center',
                    sortable: false,
                    editable: true,
                    edittype: 'select',
                    editoptions: {value: "男:男;女:女;"}
                },
                {name: 'city', align: 'center', sortable: false, editable: true},
                {name: 'sign', align: 'center', sortable: false, editable: true},
                {name: 'mobile', align: 'center', sortable: false, editable: true},
                {
                    name: 'status',
                    align: 'center',
                    sortable: false,
                    editable: true,
                    edittype: "select",
                    editoptions: {value: "1:正常;2:冻结"},
                    formatter: function (value, option, rows) {
                        if (value == 1) return "<a class='btn btn-success' onclick='modifyUserStatus(\"" + rows.id + "\",\"" + value + "\");'>正常</a>";
                        else return "<a class='btn btn-danger'onclick='modifyUserStatus(\"" + rows.id + "\",\"" + value + "\");'>冻结</a>";
                    }
                },
                {name: 'regTime', sortable: false, align: 'center'}
            ],
            autowidth: true,
            height: "auto",
            pager: "#userTablePage",
            page: 1,
            rowList: [5, 10, 20, 30],
            rowNum: 10,
            viewrecords: true,
            hiddengrid: false,
            hidegrid: true,
            editurl: '${path}/user/editUser'
        }).navGrid(
            '#userTablePage',//参数1: 分页工具栏id
            {add: true, edit: false, del: false, search: false},   //参数2:开启工具栏编辑按钮
            {},//编辑面板的配置
            {
                editData: {id: ''},
                closeAfterAdd: true,
                afterSubmit: function (response) {  //添加成功之后执行的内容
                    //1.数据入库    文件数据不对   文件没有上传
                    //2.文件异步上传   添加成功之后  上传
                    //3.修改文件路径    99（id,要的的字段内容）
                    let id = response.responseJSON.rows.id;
                    $.ajaxFileUpload({
                        url: "${path}/user/upHeadShow",
                        type: "post",
                        data: {"id": id},
                        fileElementId: "headShow", //文件选择框的id属性，即<input type="file">的id
                        success: function () {
                            //上传成功 所作的事情
                            //刷新页面
                            $("#userTable").trigger("reloadGrid");
                        }
                    });
                    return ["ok"];
                }
            },//添加面板的配置
            {});//删除的配置);

        function aliyun() {
            let phone = $('#phoneNumbers').val();
            if (phone != null && phone != '') {
                $.post('${path}/user/aliyun', {'phoneNumbers': phone}, function (data) {
                    alert(data.message);
                    $('#phoneNumbers').val("");
                    time = 60;
                    let timer = window.setInterval(
                        function getTime() {
                            time--;
                            if (time == 0) {
                                $('#phoneNumbersButton').attr('class', 'btn btn-info').removeAttr('disabled');
                                $("#phoneNumbersButton").text('发送验证码');
                                clearInterval(timer);
                            } else {
                                $('#phoneNumbersButton').attr('class', 'btn btn-default').attr('disabled', 'disabled');
                                $("#phoneNumbersButton").text(time + 's后重发');
                            }
                        }, 1000);
                }, 'json');
            }
        }

        $('#phoneNumbersButton').click(function () {
            aliyun();
        });

        //导出用户信息
        $('#exportExcel').click(function () {
            $.post("${path}/user/exportExcel", function (data) {
                alert(data.message);
            }, 'json');
        });
    });
</script>
<!--用户展示-->
<div class="col-sm-10">
    <div class="panel panel-info">
        <div class="panel-heading" style="height: 60px"><h3 style="margin-top: 10px">用户信息</h3></div>
        <div class="panel-body" style="padding: 10px 0px;padding-bottom: 0px">
            <div class="col-sm-12">
                <div class="col-sm-6">
                    <p>
                        <a href="#" id="exportExcel" class="btn btn-info">导出用户信息</a>
                        <a href="#" class="btn btn-success">导入用户信息</a>
                        <a href="#" class="btn btn-warning">测试按钮</a>
                    </p>
                </div>
                <div class="col-sm-6">
                    <div class="input-group">
                        <input type="text" class="form-control" id="phoneNumbers" placeholder="请输入手机号"
                               aria-describedby="basic-addon2">
                        <span class="input-group-btn">
                            <button class="btn btn-info" id="phoneNumbersButton" type="button">发送验证码</button>
                        </span>
                    </div>
                </div>
            </div>
            <div class="col-sm-12" style="padding-left: 0px;padding-right: 0px">
                <table id="userTable"></table>
                <div id="userTablePage"></div>
            </div>
        </div>
    </div>
</div>