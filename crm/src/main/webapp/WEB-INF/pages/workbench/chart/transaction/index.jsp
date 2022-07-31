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
            //发送查询请求
            $.ajax({
                url:'workbench/chart/transaction/queryCountOfTranGroupByStage.do',
                type:'post',
                dataType:'json',
                success:function (data) {
                    //当容器加载完成后，对容器调用工具函数
                    //基于准备好的dom，，初始化echarts实例
                    var myChat=echarts.init(document.getElementById('main'))
                    //根据图标的配置项和数据
                    var option={
                        title:{//标题
                            text:'交易统计表',
                            subtext: '交易表中各个阶段的数量'
                        },
                        tooltip:{//提示框
                            trigger:'item',
                            formatter:'{a}<br/>{b}:{c}'
                        },
                        toolbox:{
                            feature:{
                                dataView:{readonly: false},
                                restore:{},
                                saveAsImage:{}
                            }
                        },
                        series:[{//系列
                            name:'数据量',//系列名称
                            type:'funnel',//系列类型  bar---柱状图  line---折线图
                            left:'10%',
                            width:'80%',
                            label:{
                                formatter: '{b}',
                            },
                            labelLine:{
                                show:true
                            },
                            itemStyle:{
                                opacity:0.7
                            },
                            emphasis:{
                                label: {
                                    position:'inside',
                                    formatter:'{b}: {c}'
                                }
                            },

                            data:data
                        }]
                    }
                    //使用刚指定的配置项和数据显示数据
                    myChat.setOption(option)
                }
            })

        })
    </script>
</head>
<body>
<%--为Echarts准备一个具备大小（宽高）的Dom--%>
<div id="main" style="width: 600px;height: 400px"></div>
</body>
</html>
