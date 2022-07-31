<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>>">
<%--    jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--    bootstrap--%>
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%--    typeahead--%>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <title>演示自动补全插件</title>
    <script type="text/javascript">
        $(function () {
            $("#customerName").typeahead({
                source:function (jquery, process) {//每次键盘弹起都会自动触发函数，可以向后端发送请求，去查询客户表中所有名称。以[]json字符串型式返回前台，赋值给source对象
                    //发送请求                        process  是个函数，能够将['XXX','XXXX',XXXX]赋值给source
                    var customerName = $("#customerName").val();
                    $.ajax({
                        url:'workbench/transaction/queryAllCustomerName.do',
                        type:'post',
                        data:{
                            customerName:jquery
                        },
                        datatype:'json',
                        success:function (data) {//['XXX','XXXX',XXXX]
                            process(data);
                        }
                    })
                }
            })
        })
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
