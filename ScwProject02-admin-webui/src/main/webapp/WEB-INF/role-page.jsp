<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-CN">
<%@include file="include-header.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="myJs/my-role.js"></script>

<script type="text/javascript">

    <%--    声明专门的函数用来在分配Auth的时候中显示Auth的树形结构数据--%>

    function fillAuthTree() {


        //    1.发送ajax数据查询Auth数据
        var ajaxRrturn = $.ajax({
            url: "${pageContext.request.contextPath}/get/all/auth.json",
            type: "get",
            dataType: "json",
            async: false
        })

        if (ajaxRrturn.status != 200) {
            layer.msg("请求处理错误 ！响应状态码是：" + ajaxRrturn.status + "说明是：" + ajaxRrturn.statusText);
            return;
        }


        //从服务器端查询到的list不需要组装成树形结构，由zTree来组装
        var authList = ajaxRrturn.responseJSON.data;

        //准备zTree进行设置到JSON对象
        var setting = {
            "data": {
                "simpleData": {
                    //true / false 分别表示 使用 / 不使用 简单数据模式
                    "enable": true,
                    "pIdKey": "categoryId"
                },
                "key": {
                    //zTree 节点数据保存节点名称的属性名称。
                    "name": "title"
                }
            },
            "check": {
                //设置 zTree 的节点上是否显示 checkbox / radio,默认为false
                "enable": true
            }

        };

        // 4.生成树形结构
        $.fn.zTree.init($("#authTreeDemo"), setting, authList);

        //调用zTreeObj对象的方法，把节点展开
        var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
        /*
        var treeObj = $.fn.zTree.getZTreeObj("tree");
       treeObj.expandAll(expandFlag);
        expandFlag = true 表示 展开 全部节点
        expandFlag = false 表示 折叠 全部节点
         */
        zTreeObj.expandAll(true);

        //    查询已分配的Auth的id组成的数组
        ajaxRrturn = $.ajax({
            "url": "${pageContext.request.contextPath}/assign/get/assigned/auth/id/by/role/id.json",
            "data": "json",
            "type": "get",
            "dataType": "json",
            "data": {
                "roleId": $("#assignRoleId").val()
            },
            "async": false
        });

        if (ajaxRrturn.status != 200) {
            layer.msg("请求处理错误 ！响应状态码是：" + ajaxRrturn.status + "说明是：" + ajaxRrturn.statusText);
            return;
        }

        //取出后台传过来authIdArray数据
        var authIdArray = ajaxRrturn.responseJSON.data;

        // console.log(authIdArray)

        //    根据authIdArray把树形结构中对应的节点勾选上
        /*
        使用zTree方法
            1. checkNode  勾选 或 取消勾选 单个节点。[setting.check.enable = true 时有效]

             treeNode                  JSON
            需要勾选 或 取消勾选 的节点数据

            请务必保证此节点数据对象 是 zTree 内部的数据对象

            checked                  Boolean
            checked = true 表示勾选节点

            checked = false 表示节点取消勾选

            省略此参数，则根据对此节点的勾选状态进行 toggle 切换

            不影响 treeNode.nocheck = true 的节点。

            checkType                  FlagBoolean
            checkTypeFlag = true 表示按照 setting.check.chkboxType 属性进行父子节点的勾选联动操作

            checkTypeFlag = false 表示只修改此节点勾选状态，无任何勾选联动操作

            checkTypeFlag = false 且 treeNode.checked = checked 时，不会触发回调函数，直接返回

            不影响父子节点中 treeNode.nocheck = true 的节点。

            callbackFlag                   Boolean
            callbackFlag = true 表示执行此方法时触发 beforeCheck & onCheck 事件回调函数

            callbackFlag = false 表示执行此方法时不触发事件回调函数

            省略此参数，等同于 false

            2.getNodeByParam
         */

        //    1.先遍历authIdArray
        for (var i = 0; i < authIdArray.length; ++i) {
            var authId = authIdArray[i];

            //    根据id查询树形结构中对应的节点
            var treeNode = zTreeObj.getNodeByParam("id", authId);

            //    将treeNode设置为被勾选
            //    checked=true,表示勾选数据     checkType=false表示只做勾选而不联动，如果为true，下面同级节点也会被选上
            zTreeObj.checkNode(treeNode, true, false);
        }

    }

    $(function () {

        //页面加载，全选按钮失效
        $("#qx").prop("checked", false);


        //       1.为分页操作准备数据,设置为全局函数
        window.keyword = "";
        window.pageNum = 1;
        window.pageSize = 5;


        //    调用执行分页的函数，显示分页的效果
        generatePage();


        //    执行查询操作
        $("#searchKeyword").click(function () {

            //    获取搜索框的数据
            var keyword = $("#keyword").val();

            //    传入全局函数中
            window.keyword = keyword;

            //    刷新分页数据
            generatePage();
        })


        //显示添加模态窗口
        $("#showAddRoleModal").click(function () {
            $("#addModal").modal("show");
        })

        //    执行保存用户操作
        $("#saveRoleBtn").click(function () {
            var roleName = $.trim($("#roleName").val());
            if (roleName == "" || roleName == null || roleName.length == 0) {
                layer.msg("操作失败!输入不能为空");
                return;
            }
            $.ajax({
                url: "${pageContext.request.contextPath}/role/add/page.json",
                data: {
                    "name": roleName
                },
                dataType: "json",
                type: "post",
                success: function (data) {
                    //取出存储在ResultEntity中的数据
                    var result = data.result;

                    if (result == "SUCCESS") {

                        //    关闭模态窗口并清除模态窗口的添加数据
                        $("#addModal").modal("hide");
                        $("#roleName").val("");

                        // 定向到最后一页
                        window.pageNum = 99999999;
                        //    刷新分页
                        generatePage();

                        layer.msg("添加成功");

                    } else {
                        layer.msg("操作失败！" + data.message);
                    }


                },
                error: function (data) {
                    layer.msg(data.status + " " + data.statusText);
                }

            })


        })

        //    使用事件委托的形式获取到修改模态框的参数并打开模态窗口
        // ①首先找到所有“动态生成”的元素所附着的“静态”元素
        // ②on()函数的第一个参数是事件类型
        // ③on()函数的第二个参数是找到真正要绑定事件的元素的选择器
        // ③on()函数的第三个参数是事件的响应函数
        $("#rolePageBody").on("click", ".editShowBtn", function () {

            //    打开模态窗口
            $("#editModal").modal("show");

            //    获取绑定在id上的参数交给全局函数
            window.roleId = this.id;


            //    获取当前的角色名称
            //获取当前事件委托中的父元素
            /*
            * $(this) 当前的事件委托中操作的元素 <button type='button' id="+r.id+" class='btn btn-primary btn-xs editShowBtn'><i class='glyphicon glyphicon-pencil '></i></button>
            * parent()  当前事件委托的父元素为   <td>
            *  prev() 返回被选元素的前一个同级元素，即<td>"+r.name+"</td>"
            *  text() 设置或返回被选元素的文本
            * */
            var roleName = $(this).parent().prev().text();

            //    交给模态窗口
            $("#editRoleName").val(roleName);

        });

        //    执行添加操作

        $("#updateRoleBtn").click(function () {
            var roleName = $.trim($("#editRoleName").val());
            if (roleName == "" || roleName == null || roleName.length == 0) {
                layer.msg("操作失败!输入不能为空");
                return;
            }

            $.ajax({
                url: "${pageContext.request.contextPath}/role/edit/page.json",
                data: {
                    "name": roleName,
                    "id": window.roleId
                },
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data) {
                        //    关闭模态窗口
                        $("#editModal").modal("hide");
                        layer.msg("修改成功！！");

                        //    重新加载分页
                        generatePage();
                    } else {
                        layer.msg("修改失败！！");

                    }

                },
                error: function () {
                    layer.msg("请求发送失败!!");

                }
            })
        })


        //测试数据
        // var roleArray=[{roleId :5,roleName:"aaa"},{roleId :2,roleName:"bbb"},{roleId :3,roleName:"444"}]
        // showConfirmModal(roleArray)
        //执行删除操作
        $("#removeRoleBtn").click(function () {


            // 从全局变量范围获取roleIdArray，转换为JSON字符串
            var requestBody = JSON.stringify(window.roleArray);

            $.ajax({
                url: "${pageContext.request.contextPath}/role/remove/page.json",
                type: "post",
                dataType: "json",
                //将全局变量转为JSON字符串后，后台使用@RequestBody注解时，这段必须加，否则无法识别
                contentType: "application/json;charset=UTF-8",
                data: requestBody,
                success: function (data) {
                    var result = data.result;
                    if (result == "SUCCESS") {
                        //关闭模态窗口
                        $("#removeModal").modal("hide");
                        layer.msg("删除成功");

                        //删除成功后将选择的按钮取消勾选
                        $("input[name=xz]").prop("checked", false);
                        $("#qx").prop("checked", false);

                        //    重新加载分页
                        generatePage();
                    } else {
                        layer.msg("操作失败！" + result);
                    }
                },
                error: function () {
                    layer.msg("请求发送失败!!");

                }
            })
        });

        //给单条删除按钮绑定单机响应函数
        $("#rolePageBody").on("click", ".removeBtn", function () {

            var roleName = $(this).parent().prev().text();
            //    创建role对象存入数组
            var roleArray = [{
                roleId: this.id,
                roleName: roleName
            }];

            //    调用专门的函数打开模态框
            showConfirmModal(roleArray);
        });

        //    给全选按钮绑定事件
        $("#qx").click(function () {

            $("input[name=xz]").prop("checked", this.checked);
        })

        //给单选按钮绑定事件
        $("#xz").click(function () {
            $("input[name=xz]").prop("checked", this.checked);
        });

        //    给单选按钮绑定事件,需要使用事件委托的形式
        $("#rolePageBody").on("click", $("input[name=xz]"), function () {

            //    假如都勾选上，全选按钮也勾选上，反之则不勾选
            //当单选按钮的数量等于选单选按钮选中的数量则全选按钮也勾选上
            $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);
        });

        //    给总的删除按钮绑定事件
        $("#removeRoleAllBtn").click(function () {

            //定义一个数组来存放删除的信息
            var roleArray = [];
            //这个获取到的只是普通元素，而$($元素)则是使用jquery获取到该元素，相等于一个元素选择器
            var $xz = $("input[name=xz]:checked");
            if ($xz.length == 0) {
                layer.msg("请选择你要删除的纪录");
                return;
            } else {
                $.each($xz, function (i, n) {
                    roleArray.push({
                        "roleId": $(n).val(),
                        //这个操作的元素为<input type='checkbox' name='xz' value=" + r.id + " name='id'>，所以父元素为<td>
                        //next().next().text()下一个的下一个兄弟元素的text为，<td>" + r.name + "</td>"
                        "roleName": $(n).parent().next().next().text()
                    })
                })
                /*
                // 尚硅谷所用方法，是不同的，我没绑定id值，也没有定义class,而是选择将id值放入value中，所用是不同的，还改了顺序，顺序也不同
			$(".summary:checked").each(function(){

				// 使用this引用当前遍历得到的多选框
				var roleId = this.id;

				// 通过DOM操作获取角色名称
				var roleName = $(this).parent().next().text();

				roleArray.push({
					"roleId":roleId,
					"roleName":roleName
				});
			});
                 */
            }

            //    打开删除的模态框
            showConfirmModal(roleArray);
        });

        //打开分配角色模态框
        $("#rolePageBody").on("click", ".checkBtn", function () {
            $("#assignModal").modal("show");

            //获得当前的roleId并放入定义好的隐藏域当中
            $("#assignRoleId").val(this.id);


            //    在模态窗口中装载树Auth的树结构
            fillAuthTree();
        })

        //    给分配角色权限按钮绑定单击事件
        $("#assignBtn").click(function () {

            //    准备一个数组存储选中节点的id
            var authIdArray = [];

            //    获取所使用的zTree对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

            //获取被勾选上的节点
            /*
           使用方法   getCheckedNodes
           获取输入框被勾选 或 未勾选的节点集合。[setting.check.enable = true 时有效]
           请通过 zTree 对象执行此方法。

            参数说明
           checked          Boolean
           checked = true 表示获取 被勾选 的节点集合
           checked = false 表示获取 未勾选 的节点集合
           省略此参数，等同于 true。
            */

            var nodeCheckedNodes = zTreeObj.getCheckedNodes(true);

            //   遍历该节点数组并push进authIdArray数组中
            $.each(nodeCheckedNodes, function (i, n) {
                //取出节点中的id
                var authId = n.id;
                //存入数组
                authIdArray.push(authId)
            })
            //    发送请求进行分配
            var respnseBody = {
                "authIdArray": authIdArray,
                // 为了服务器端 handler 方法能够统一使用 List<Integer>方式接收数据，roleId 也存入数组
                "roleId": [$("#assignRoleId").val()]
            }

            //    若传数组需要转化为json
            respnseBody = JSON.stringify(respnseBody);
            //    发送ajax请求
            $.ajax({
                "url": "${pageContext.request.contextPath}/assign/do/role/assign/auth.json",
                "dataType": "json",
                "data": respnseBody,
                "type" : "post",
                //    并且设置内容类型
                "contentType": "application/json;charset=UTF-8",
                "success": function (data) {

                    var result = data.result;
                    if (result == "SUCCESS") {
                        layer.msg("执行成功！！");
                        $("#assignModal").modal("hide");
                    } else {
                        layer.msg("操作失败！!" + data.message);
                    }
                },
                error: function (data) {
                    layer.msg(data.status + " " + data.statusText);
                }
            })
        })
    });
</script>
<body>
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
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input class="form-control has-success" id="keyword" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" id="searchKeyword" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" id="removeRoleAllBtn" class="btn btn-danger "
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" id="showAddRoleModal" class="btn btn-primary" style="float:right;"><i
                            class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30"><input type="checkbox" id="qx"></th>
                                <th width="30">#</th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">
                            </tbody>
                            <tfoot>
                            <tr>
                                <%--     这里显示分页                          --%>
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

<%@include file="modal-role-add.jsp" %>
<%@include file="modal-role-edit.jsp" %>
<%@include file="modal-role-remove.jsp" %>
<%@include file="modal-role-assign-auth.jsp" %>

</body>
</html>

