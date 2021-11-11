<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <base
            href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-2.1.1.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <script type="text/javascript" src="layer/layer.js"></script>
    <style>

    </style>
</head>

<script>
    $(function () {
        $("#btn").click(function () {
            //浏览器的后退功能
            window.history.back();
        })
    })

</script>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
    </div>
    </div>
</nav>

<div class="container">
    <h2 class="form-signin-heading" align="center"><i class="glyphicon glyphicon-log-in"></i> 尚筹网系统消息</h2>

    <%--
      requestScope对应的是存放request域数据的map
      requestScope.exception相当于request.getAttribute("exception")
      requestScope.exception.message相当于exception.getMessage()
    --%>

    <p style="font-size: 20px;margin: 0 auto"> ${requestScope.exception.message}</p>
    <br>
    <button id="btn" style="width: 150px;margin: 0 auto" class="btn btn-lg btn-success btn-block">返回上一步</button>

</div>
<script src="${pageContext.request.contextPath}/jquery/jquery-2.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>