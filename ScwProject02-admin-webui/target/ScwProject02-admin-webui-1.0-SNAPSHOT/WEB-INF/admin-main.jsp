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
            <%@include file="include-main.jsp" %>

            <p>显示出来才发现，Principal原来是我们自己封装的SecurityAdmin对象</p>
            <br>
            <p>SpringSecurity处理完登录之后把登录成功的User对象以principal属性存入UserNamePrincipal中</p>
            <br>
            Principal: <security:authentication property="principal.class.name"/>
            <br>
            <p>那么直接访问SecurityAdmin中的originAdmin属性中的userName即可获得用户名</p>
            userName: <security:authentication property="principal.originAdmin.userName"/> <br>
            userPswd: <security:authentication property="principal.originAdmin.userPswd"/><br>
            loginAcct: <security:authentication property="principal.originAdmin.loginAcct"/><br>
            email: <security:authentication property="principal.originAdmin.email"/><br>
            createTime: <security:authentication property="principal.originAdmin.createTime"/> <br>
            <br><br>
            <security:authorize access="hasAnyRole('经理')">
                <p>只有拥有经理角色的用户才能看到这段文字</p>
            </security:authorize>

            <security:authorize access="hasAuthority('role:delete')">
                <p>只有拥有角色删除权限的用户才能看到这段文字</p>
            </security:authorize>


        </div>
    </div>
</div>
</body>
</html>

