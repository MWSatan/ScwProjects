<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">
<%@include file="include-header.jsp" %>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>


<script>

    $(function () {

        //   1.创建JSON对象用于存储zTree的数据
        var setting = {};

        //    2.准备生成zTree数据，发送ajax请求得到数据
        $.ajax({
            url : "${pageContext.request.contextPath}/menu/get/whole/tree.json",
            type : "get",
            dataType : "json",
            success : function (data) {

                var result = data.result;
                if (result=="SUCCESS"){
                    //取出真实数据
                    var zNodes = data.data;

                    //    拿到数据后初始化树形结构
                    $.fn.zTree.init($("#treeDemo"),setting,zNodes);

                }else {
                    layer.msg("error!!!   "+data.message);
                }

            },
            error : function (data) {
              layer.msg("请求发送失败");
            }
        })

    })
</script>


<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
<%--                    这个ul标签是zTree动态生成的节点所依附的静态节点--%>
                    <ul id="treeDemo" class="ztree">
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

