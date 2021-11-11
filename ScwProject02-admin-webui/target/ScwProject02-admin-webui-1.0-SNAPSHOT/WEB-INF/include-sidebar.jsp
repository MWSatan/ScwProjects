<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="col-sm-3 col-md-2 sidebar">
    <div class="tree">
        <ul style="padding-left:0px;" class="list-group">
            <li class="list-group-item tree-closed" >
                <a href="javascript:void(0)"><i class="glyphicon glyphicon-dashboard"></i> 控制面板</a>
            </li>
            <li class="list-group-item tree-closed">
                <span><i class="glyphicon glyphicon glyphicon-tasks"></i> 权限管理 <span class="badge" style="float:right">3</span></span>
                <ul style="margin-top:10px;display:none;">
                    <li style="height:30px;">
                        <a href="${pageContext.request.contextPath}/admin/get/page.html"><i class="glyphicon glyphicon-user"></i> 用户维护</a>
                    </li>
                    <li style="height:30px;">
                        <a href="${pageContext.request.contextPath}/role/to/page.html"><i class="glyphicon glyphicon-king"></i> 角色维护</a>
                    </li>
                    <li style="height:30px;">
                        <a href="${pageContext.request.contextPath}/menu/to/page.html"><i class="glyphicon glyphicon-lock"></i> 菜单维护</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</div>