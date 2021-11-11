<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">

<%@include file="include-header.jsp" %>


<script type="text/javascript">

    $(function () {

        $("#toRightBtn").click(function () {

            //select:eq(数字)，表示取得页面中select标签选择器，0表示第一个,>表示选择子元素
            //option:selected 表示获得被选中的子元素
            //appendTo("selector") 参数selector表示被选的元素，即把content内容插入selector元素内，默认是在尾部。

            /*
            append 和 appendTo的区别
            append用于追加内容，可以是标签、内容等
            appendTo用于追加选择器，比如appendTo("#id"),默认在尾部
             */
            $("select:eq(0)>option:selected").appendTo("select:eq(1)")
        })

        $("#toLeftBtn").click(function () {

            $("select:eq(1)>option:selected").appendTo("select:eq(0)")
        })

        //    修改提交的时候已分配角色中未选中的角色无法提交到数据库得bug
        //    解决方案：将已分配角色中的列表全部选中,使用prop
        $("#subBtn").click(function () {
            //在这里因为我们明确了属性所以可以用attr
            // $("select:eq(1)>option").attr("selected", "selected");
            $("select:eq(1)>option").prop("selected", "selected");
        })
    })
</script>
<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form role="form" action="${pageContext.request.contextPath}/assign/save/assign/role/page.html"
                          method="post" enctype="application/x-www-form-urlencoded" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select class="form-control" multiple="" size="10" style="width:100px;height: 224px">
                                <c:forEach items="${unAssignRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>

                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="" size="10"
                                    style="width:100px;height: 224px">
                                <c:forEach items="${assignRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" id="subBtn" style="width: 150px;margin-top: 35px;margin-left: 90px;"
                                class="btn btn-lg btn-success btn-block">保存
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

