<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2022/7/13
  Time: 18:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function () {
            //给下载按钮添加单机事件
            $("#fileDownloadBtn").click(function (){
                //发送文件下载的请求
                window.location.href="workbench/activity/fileDownload.do";
            });
        })

    </script>
</head>
<body>
<input type="button" value="下载" id="fileDownloadBtn">
</body>
</html>
