<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <%--引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--引入插件--%>
    <script type="text/javascript" src="jquery/echars/echarts.min.js"></script>
    <title>演示echarts插件</title>
    <script type="text/javascript">
        $(function () {
            //当容器加载完成后，对容器调用工具函数
            //基于准备好的dom，，初始化echarts实例
            var myChat=echarts.init(document.getElementById('main'))
            //根据图标的配置项和数据
            var option={
                title:{//标题
                    text:'Echarts 入门实例'
                },
                tooltip:{//提示框

                },
                legend:{//图例
                    data:['销量']
                },
                xAxis:{//x轴，数据项轴
                    data:['衬衫','羊毛衫','雪纺织','裤子','高跟鞋','袜子']
                },
                yAxis:{},//数据图
                series:[{//系列
                        name:'销量',//系列名称
                        type:'bar',//系列类型  bar---柱状图  line---折线图
                        data:[5,20,36,10,10,20]//系列数据
                    }]
            }
            //使用刚指定的配置项和数据显示数据
            myChat.setOption(option)
        })
    </script>
</head>
<body>
<%--为Echarts准备一个具备大小（宽高）的Dom--%>
<div id="main" style="width: 600px;height: 400px"></div>
</body>
</html>
