<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">
<%@include file="include-header.jsp" %>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="myJs/my-menu.js"></script>


<script>

    $(function () {
        generateZTree();

        //    添加子节点
        $("#treeDemo").on("click", ".addBtn", function () {

            //获取当前的id,因为得跨域，所以需要使用全局变量,暂时先不用
            // window.pid = this.id;

            $("#menuPid").val(this.id);


            //打开模态窗口

            $("#addMenuModal").modal("show");

            return false;
        })

        //    执行添加操作
        $("#saveMenuBtn").click(function () {


            var menuName = $.trim($("#menuName").val());
            var menuUrl = $.trim($("#menuUrl").val());

            //定位addMenuModal模态窗口中被选中的icon图标并获取值
            var menuIcon = $("#addMenuModal input[name=icon]:checked").val();
            var menuPid = $.trim($("#menuPid").val());

            if (menuName == null || menuName.length == 0 || menuUrl == null ||
                menuUrl.length == 0 || menuIcon == null) {
                layer.msg("输入不能为空")
                return;
            }

            $.ajax({
                url: "${pageContext.request.contextPath}/menu/add/tree.json",
                type: "post",
                dataType: "json",
                data: {
                    "pid": menuPid,
                    "name": menuName,
                    "url": menuUrl,
                    "icon": menuIcon
                },
                success: function (data) {

                    if (data.result == "SUCCESS") {
                        layer.msg("添加成功");

                        //清空表单
                        $("#menuName").val("")
                        $("#menuUrl").val("")

                        $("#addMenuModal [name=icon]").val([]);
                        $("#menuPid").val("");
                        //关闭模态窗口
                        $("#addMenuModal").modal("hide");

                        //刷新树结构
                        generateZTree();
                    } else {
                        layer.msg("添加失败！" + data.message);
                    }
                },
                error: function (data) {
                    console.log(data.status + " " + data.statusText)
                }
            })

            //    清空表单
            //    jquery不传任何参数相当于用户自动点击了一下
            $("#resetBtn").click();
        })

        //    打开修改模态框
        //    这个修改其实可以将这个id传给后台去查然后回显数据，但是zTree里面应该也有这些数据，所以试下新的方法
        $("#treeDemo").on("click", ".editBtn", function () {

            $("#editMenuId").val(this.id);


            //获取zTreeObj对象,使用方法 $.fn.zTree.getZTreeObj("Dom容器Id"),而这个id已经在控制器里输出过了， 就是treeDemo
            /*
            zTree v3.x 专门提供的根据 treeId 获取 zTree 对象的方法。
            必须在初始化 zTree 以后才可以使用此方法。

            有了这个方法，用户不再需要自己设定全局变量来保存 zTree 初始化后得到的对象了，而且在所有回调函数中全都会返回 treeId 属性，
            用户可以随时使用此方法获取需要进行操作的 zTree 对象

            Function           参数说明
            treeId                     String
            zTree 的 DOM 容器的 id

            返回值JSON
            zTree 对象，提供操作 zTree 的各种方法，对于通过 js 操作 zTree 来说必须通过此对象
                         */
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            //使用zTree来查找当前节点的数据
            //使用方法为   zTreeObj.getNodeByParam
            //
            /*
            zTreeObj.getNodeByParam，根据节点数据的属性搜索，获取条件完全匹配的节点数据 JSON 对象集合
            key          String
            需要精确匹配的属性名称

            value           ?
            需要精确匹配的属性值，可以是任何类型，只要保证与 key 指定的属性值保持一致即可

            parentNodeJSON
            可以指定在某个父节点下的子节点中搜索

            忽略此参数，表示在全部节点中搜索

            返回值Array(JSON)
            匹配精确搜索的节点数据集合

            如无结果，返回 [ ]
             */

            var value = this.id;
            //通过此方法获得当前节点，使用id的形式
            var currentNode = zTreeObj.getNodeByParam("id", value);

            //赋值给修改模态窗口中的表单中

            $("#editMenuName").val(currentNode.name);
            $("#editMenuUrl").val(currentNode.url);
            // 回显 radio 可以这样理解：被选中的 radio 的 value 属性可以组成一个数组，
            // 然后再用这个数组设置回 radio，就能够把对应的值选中
            $("#editMenuModal input[name=icon]").val([currentNode.icon]);


            //打开模态窗口
            $("#editMenuModal").modal("show");


            return false;
        });

        //    执行修改方法
        $("#editMenuBtn").click(function () {

            var menuName = $.trim($("#editMenuName").val());
            var menuUrl = $.trim($("#editMenuUrl").val());

            //定位addMenuModal模态窗口中被选中的icon图标并获取值
            var menuIcon = $("#editMenuModal input[name=icon]:checked").val();
            var menuId = $.trim($("#editMenuId").val());


            if (menuName == null || menuName.length == 0 || menuUrl == null ||
                menuUrl.length == 0 || menuIcon == null) {
                layer.msg("输入不能为空")
                return;
            }


            $.ajax({
                url: "${pageContext.request.contextPath}/menu/edit/tree.json",
                type: "post",
                dataType: "json",
                data: {
                    "id": menuId,
                    "name": menuName,
                    "url": menuUrl,
                    "icon": menuIcon
                },
                success: function (data) {

                    if (data.result == "SUCCESS") {
                        layer.msg("修改成功");

                        //关闭模态窗口
                        $("#editMenuModal").modal("hide");

                        //刷新树结构
                        generateZTree();
                    } else {
                        layer.msg("修改失败！" + data.message);
                    }
                },
                error: function (data) {
                    console.log(data.status + " " + data.statusText)
                }
            })

        })

        //    打开删除的模态窗口
        $("#treeDemo").on("click", ".removeBtn", function () {

            //打开模态窗口，并弹出提示信息，图标加名称
            $("#removeMenuId").val(this.id);

            //从zTree里面取图标和名称
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            var currentNode = zTreeObj.getNodeByParam("id", this.id);

            console.log(currentNode)

            $("#removeMessage").html("是否删除【<i class='"+ currentNode.icon +"'></i>"+ currentNode.name + "】这个节点？");

            $("#removeMenuModal").modal("show");

            return false;
        })

        //    执行删除操作
        $("#removeMenuBtn").click(function () {
            $.ajax({
                url: "${pageContext.request.contextPath}/menu/remove/tree.json",
                type: "post",
                dataType: "json",
                data: {
                    "id": $("#removeMenuId").val(),
                },
                success: function (data) {

                    if (data.result == "SUCCESS") {
                        layer.msg("删除成功");

                        //关闭模态窗口
                        $("#removeMenuModal").modal("hide")

                        //刷新树结构
                        generateZTree();
                    } else {
                        layer.msg("删除失败！" + data.message);
                    }
                },
                error: function (data) {
                    console.log(data.status + " " + data.statusText)
                }
            })
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


    <%--    引入模态窗口--%>

    <%@include file="modal-menu-add.jsp" %>
    <%@include file="modal-menu-edit.jsp" %>
    <%@include file="modal-menu-remove.jsp" %>

</div>
</body>
</html>

