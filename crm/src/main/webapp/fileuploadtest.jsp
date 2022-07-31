<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2022/7/14
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%--文件上传的表单三个条件：
    1.表单组件标签只能用：<input type="file">
    2.请求只能使用：post
             post:参数通过请求体提交到后台，既能提交文件数据也能提交二进制数据；理论上对参数长度没有限制；相对安全；效率相对较低
             get:参数通过请求头提交到后台，参数放在url后面；只能向后天提交文本数据；对参数长度有限制；数据不安全；效率高
    3.表单的编码格式只能用:multipart/form-data
        根据HTTP协议的规定，浏览器每次向后台提交参数，都会对参数进行统一的编码；默认采用的是urlencoded，这种编码格式只能对文本数据编码：
        浏览器每次向后台提交参数，都会首先把所有的参数转换成字符串，然后对这些数据统一进行urlencoded编码；
        文本上传的表单编码格式只能用multipart/form-data
--%>

<form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile"><br>
    <input type="text" name="userName"><br>
    <input type="submit" name="提交">
</form>
</body>
</html>
