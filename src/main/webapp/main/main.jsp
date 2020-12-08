<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>应学APP后台管理系统</title>
    <link rel="icon" href="${path}/bootstrap/img/arrow-up.png" type="image/x-icon">
    <link rel="stylesheet" href="${path}/bootstrap/css/bootstrap.css">

    <%--引入jqgrid中主题css--%>
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/css/css/hot-sneaks/jquery-ui-1.8.16.custom.css">
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/boot/css/trirand/ui.jqgrid-bootstrap.css">
    <%--引入js文件--%>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/bootstrap/js/bootstrap.js"></script>
    <script src="${path}/bootstrap/jqgrid/js/i18n/grid.locale-cn.js"></script>
    <script src="${path}/bootstrap/jqgrid/boot/js/trirand/jquery.jqGrid.min.js"></script>
    <script src="${path}/bootstrap/js/ajaxfileupload.js"></script>
    <script src="${path}/bootstrap/js/echarts.min.js"></script>
    <script src="${path}/bootstrap/js/china.js"></script>
    <script type="text/javascript" src="${path}/bootstrap/js/goeasy-1.0.5.js"></script>

</head>
<body>
<!--导航条-->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <img class="navbar-brand" src="${path}/bootstrap/img/yx-icon.png"/>
            <a class="navbar-brand" href="#">应学视频APP后台管理系统<small>v1.0</small></a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse navbar-right" id="bs-example-navbar-collapse-1">
            <c:if test="${sessionScope.admin!=null}">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href=""><span class="text">欢迎: </span> <span
                            class="text text-info">${sessionScope.admin.username}</span></a></li>
                    <li><a href="${path}/admin/exit">退出<span class="glyphicon glyphicon-share"></span></a></li>
                </ul>
            </c:if>
            <c:if test="${sessionScope.admin==null}">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${path}/login/login.jsp">登录</a></li>
                </ul>
            </c:if>
        </div>
    </div>
</nav>

<!--主体-->
<div class="container-fluid">
    <!--第一行-->
    <div class="row">
        <!--左边手风琴-->
        <div class="col-sm-2">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default panel-info">
                    <div class="panel-heading" role="tab" id="headingOne">
                        <h4 class="panel-title text-center">
                            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne"
                               aria-expanded="true" aria-controls="collapseOne">
                                <span class="glyphicon glyphicon-user"></span> 用户管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                        <div class="panel-body text-center" style="padding-bottom: 0px">
                            <ul class="list-group">
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-info" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/user/UserList.jsp');">用户展示</a>
                                </li>
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-info" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/user/UserStatistics.jsp');">用户统计</a>
                                </li>
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-info" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/user/UserDistribution.jsp');">用户分布</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default panel-success">
                    <div class="panel-heading" role="tab" id="headingTwo">
                        <h4 class="panel-title text-center">
                            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                               href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                <span class="glyphicon glyphicon-th-list"></span> 分类展示
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                        <div class="panel-body text-center" style="padding-bottom: 0px">
                            <ul class="list-group">
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-success" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/category/Category.jsp');">分类列表</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default panel-warning">
                    <div class="panel-heading text-center" role="tab" id="headingThree">
                        <h4 class="panel-title">
                            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                               href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                <span class="glyphicon glyphicon-film"></span> 视频管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse" role="tabpanel"
                         aria-labelledby="headingThree">
                        <div class="panel-body text-center" style="padding-bottom: 0px">
                            <ul class="list-group">
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-warning" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/video/videoShow.jsp');">视频展示</a>
                                </li>
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-warning" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/video/searchVideo.jsp');">视频搜索</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default panel-primary">
                    <div class="panel-heading text-center" role="tab" id="headingFour">
                        <h4 class="panel-title">
                            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                               href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                                <span class="glyphicon glyphicon-tag"></span> 反馈管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFour" class="panel-collapse collapse" role="tabpanel"
                         aria-labelledby="headingFour">
                        <div class="panel-body text-center" style="padding-bottom: 0px">
                            <ul class="list-group">
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-primary" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/feedback/feedbackShow.jsp')">反馈列表</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default panel-danger">
                    <div class="panel-heading" role="tab" id="headingFire">
                        <h4 class="panel-title text-center">
                            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                               href="#collapseFire" aria-expanded="false" aria-controls="collapseFire">
                                <span class="glyphicon glyphicon-calendar"></span> 日志管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFire" class="panel-collapse collapse" role="tabpanel"
                         aria-labelledby="headingFire">
                        <div class="panel-body text-center" style="padding-bottom: 0px">
                            <ul class="list-group">
                                <li class="list-group-item"
                                    style="margin-bottom: 5px;padding: 0px;border: 2px solid white">
                                    <a class="btn btn-danger" href="javascript:;"
                                       onclick="javascript:$('#content').load('${path}/log/logShow.jsp')">日志列表</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <!--右边内容-->
        <div id="content">
            <div class="col-sm-10">
                <!--巨幕-->
                <div class="col-sm-12">
                    <div class="jumbotron" style="padding: 10px">
                        <h2 class="text-center">欢迎来到应学APP后台管理系统</h2>
                    </div>
                </div>
                <!--轮播图-->
                <div class="col-sm-12">
                    <div id="carousel-example-generic" data-interval="3000" class="carousel slide" data-ride="carousel">
                        <!-- Indicators -->
                        <ol class="carousel-indicators">
                            <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                            <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                            <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                            <li data-target="#carousel-example-generic" data-slide-to="3"></li>
                        </ol>

                        <!-- Wrapper for slides -->
                        <div class="carousel-inner" role="listbox">
                            <div class="item active text-center">
                                <img src="${path}/bootstrap/img/04.jpg" alt="..."
                                     style="width: 900px;margin-left:13.5%">
                                <div class="carousel-caption">
                                </div>
                            </div>
                            <div class="item">
                                <img src="${path}/bootstrap/img/03.jpg" alt="..."
                                     style="width: 900px;margin-left:13.5%">
                                <div class="carousel-caption">
                                </div>
                            </div>
                            <div class="item">
                                <img src="${path}/bootstrap/img/02.jpg" alt="..."
                                     style="width: 900px;margin-left:13.5%">
                                <div class="carousel-caption">
                                </div>
                            </div>
                            <div class="item">
                                <img src="${path}/bootstrap/img/01.jpg" alt="..."
                                     style="width: 900px;margin-left:13.5%">
                                <div class="carousel-caption">
                                </div>
                            </div>
                        </div>

                        <!-- Controls -->
                        <a class="left carousel-control" href="#carousel-example-generic" role="button"
                           data-slide="prev">
                            <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        </a>
                        <a class="right carousel-control" href="#carousel-example-generic" role="button"
                           data-slide="next">
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--页脚-->
<nav class="navbar navbar-default navbar-fixed-bottom" style="bottom: -24px">
    <div class="container">
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
            <ul class="nav navbar-nav" style="margin-left: 40%">
                <li style="margin-top: 3px"><p class="text-center" style="margin-right: 0px">
                    @百知教育zhangcn@apzrkhr.com</p></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

</body>
</html>
