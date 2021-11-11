<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">

<%@include file="include-header.jsp" %>

<script src="jquery/jquery.pagination.js" type="text/javascript"></script>
<link href="css/pagination.css" rel="stylesheet">
<body>

<script type="text/javascript">
    $(function () {
        // 自定义分页函数
        initPagination();


    });

    //初始化页码导航条的函数
    function initPagination() {

        //    获取总记录数
        var totalRecord = ${pageInfo.total};


        //    声明一个json对象来存储Pagination要设置的属性
        var properties = {
            //如1 2 3...20 21 22...50 51 52,即在左右两边的显示的几条页码即为边缘页，中间为主体页
            num_edge_entries: 3,     //边缘页
            num_display_entries: 5,    //主体页
            callback: pageSelectCallback,  //回调函数
            items_per_page: ${pageInfo.pageSize},//每页显示的纪录数
            current_page: ${pageInfo.pageNum - 1},//设置一开始的页码,Pagination内部需要将页码设置为0才能用
            prev_text: "上一页", //上一页按钮文本
            next_text: "下一页" //下一页按钮文本
        };


        /**  生成页码导航条
         *   totalRecord 总纪录数 int类型
         *   properties 各个参数
         */
        // 生成页码导航条
        $("#Pagination").pagination(totalRecord, properties);

    }

    //实现分页页面跳转功能，就是点击第几页第几页这些的

    /**
     *
     * @param pageIndex  从0开始的页码
     * @param Jquery
     */
    function pageSelectCallback(pageIndex, jQuery) {

        //根据pageIndex得到pageNum
        var pageNum = pageIndex + 1;//因为在初始化函数设置为了0，则在我们使用的时候需要+再来使用

        var keyword = $("#keyword").val();

        //跳转页面，将跳转的页面传入controller的分页方法
        window.location.href = "${pageContext.request.contextPath}/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;

        //由于每一个按钮都是超链接，所以这个函数最后需要取消超链接的默认行为
        return false;
    }

    function del(adminId, pageNum, keyword) {

        if (confirm("确定删除吗？")) {
            $.ajax({
                //淦，使用json时如果web容器中配置了放行json为.json,则后缀也得为json
                url: "${pageContext.request.contextPath}/admin/remove/" + adminId + ".json",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                type: "post",
                success: function (data) {
                    if (data) {
                        alert("删除成功")
                        if (typeof (keyword) != "undefined" && keyword != null) {
                            window.location.href = "${pageContext.request.contextPath}/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
                        } else {
                            window.location.href = "${pageContext.request.contextPath}/admin/get/page.html?pageNum=" + pageNum;
                        }
                    } else {
                        layer.msg("删除失败，可能删除的为当前用户")
                    }
                },
                error: function () {
                    console.log("请求发送错误，请检查")
                }

            })

        }
    }

</script>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form action="${pageContext.request.contextPath}/admin/get/page.html" method="post"
                          class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keyword" name="keyword" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" class="btn btn-danger" id="delBtn" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>

                    <a href="${pageContext.request.contextPath}/admin/to/add/page.html" class="btn btn-primary"
                       style="float:right;"><i class="glyphicon glyphicon-plus"></i>新增</a>

                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th width="30"><input type="checkbox"></th>
                                <th width="30">#</th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${empty requestScope.pageInfo.list}">
                                <tr>
                                        <%--                                    合并单元格--%>
                                    <td colspan="6" align="center">抱歉，没有查询到您的数据</td>
                                </tr>
                            </c:if>
                            <%--                            因为存储在pageInfo的是list集合，所以说是pageInfo.list,pageInfo.list还是PageInfo的属性 --%>
                            <c:if test="${!empty requestScope.pageInfo.list}">
                                <%--                                varStatus="mystatus" 循环标签，count为从1开始循环 --%>
                                <%--                               requestScope可省略 --%>
                                <c:forEach items="${pageInfo.list}" var="admin" varStatus="mystatus">
                                    <tr>
                                        <td><input type="checkbox"></td>
                                        <td>${mystatus.count}</td>
                                        <td>${admin.loginAcct}</td>
                                        <td>${admin.userName}</td>
                                        <td>${admin.email}</td>
                                        <td>
                                                <%--<button type="button" class="btn btn-success btn-xs"><i
                                                        class=" glyphicon glyphicon-check"></i></button>--%>

                                            <a href="${pageContext.request.contextPath}/assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${pageInfo.pageNum}&keyword=${param.keyword}"
                                               class="btn btn-success btn-xs"><i
                                                    class="  glyphicon glyphicon-check"></i></a>


                                            <a href="${pageContext.request.contextPath}/admin/to/edit/page.html?adminId=${admin.id}&pageNum=${pageInfo.pageNum}&keyword=${param.keyword}"
                                               class="btn btn-primary btn-xs"><i
                                                    class=" glyphicon glyphicon-pencil"></i></a>

                                                <%--旧的href="${pageContext.request.contextPath}/remove.html?adminId="+id --%>
                                                <%--使用resful风格，搭配@PathVariable注解--%>
                                                <%--@PathVariable @PathVariable是spring3.0的一个新功能：接收请求路径中占位符的值--%>

                                            <button type="button" class="btn btn-danger btn-xs"
                                                    onclick="del(${admin.id},${pageInfo.pageNum},${param.keyword})"><i
                                                    class=" glyphicon glyphicon-remove"></i></button>

                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>

                            </tbody>
                            <tfoot>
                            <tr>
                                <%--
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                        <li class="disabled"><a href="#">上一页</a></li>
                                        <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
                                        <li><a href="#">1</a></li>
                                        <li><a href="#">3</a></li>
                                        <li><a href="#">4</a></li>
                                        <li><a href="#">5</a></li>
                                        <li><a href="#">下一页</a></li>
                                    </ul>
                                </td>
                            </tr>
                            --%>

                                <%--                            使用pageination替换分页信息 --%>
                                <!-- 这里显示分页 -->
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

