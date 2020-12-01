<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
    $(function () {
        $.post('${path}/user/queryCountByMonth', function (data) {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('UserStatistics'));
            let list = [];
            $.each(data.months, function (index, value) {
                list.push(value + "月");
            });

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '应学APP用户注册信息柱状图'
                },
                tooltip: {},
                legend: {
                    data: ['男', '女']
                },
                xAxis: {
                    data: list
                },
                yAxis: {},
                series: [{
                    name: '男',
                    type: 'bar',
                    data: data.boysCount
                }, {
                    name: '女',
                    type: 'bar',
                    data: data.girlsCount
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        });
        var goEasy = new GoEasy({
            host: 'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey: "BC-cf8d6c8f2ae84dcc93eaf76dc7a99702", //替换为您的应用appkey
        });
        goEasy.subscribe({
            channel: "test-channel", //替换为您自己的channel
            onMessage: function (message) {
                console.log("Channel:" + message.channel + " content:" + message.content);
                var myChart = echarts.init(document.getElementById('UserStatistics'));
                let data = JSON.parse(message.content)
                let list = [];
                $.each(data.months, function (index, value) {
                    list.push(value + "月");
                });

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '应学APP用户注册信息柱状图'
                    },
                    tooltip: {},
                    legend: {
                        data: ['男', '女']
                    },
                    xAxis: {
                        data: list
                    },
                    yAxis: {},
                    series: [{
                        name: '男',
                        type: 'bar',
                        data: data.boysCount
                    }, {
                        name: '女',
                        type: 'bar',
                        data: data.girlsCount
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        });
    })

</script>
<!--用户分布图-->
<div class="col-sm-10">
    <div id="UserStatistics" style="width: 1200px;height:500px;"></div>
</div>