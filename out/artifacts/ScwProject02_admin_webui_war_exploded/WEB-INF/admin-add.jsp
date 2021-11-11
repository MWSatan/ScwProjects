<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">
<%@include file="include-header.jsp" %>
<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/main/page.html.json">首页</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/get/page.html">数据列表</a></li>
                <li class="active">新增</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <p style="color: red">${exception.message}</p>
                <div class="panel-body">
                    <form role="form" method="post" action="${pageContext.request.contextPath}/admin/save.html">
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆账号</label>
                            <input type="text" name="loginAcct" required="required" class="form-control"
                                   id="exampleInputPassword1" placeholder="请输入登陆账号">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆密码</label>
                            <input type="text" required="required" name="userPswd" class="form-control"
                                   id="exampleInputPassword1" placeholder="请输入登陆密码">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">用户昵称</label>
                            <input type="text" class="form-control" name="userName" required="required"
                                   id="exampleInputPassword1" placeholder="请输入用户昵称">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">邮箱地址</label>
                            <input type="email" class="form-control" name="email" required="required"
                                   id="exampleInputEmail1" placeholder="请输入邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 新增
                        </button>
                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

