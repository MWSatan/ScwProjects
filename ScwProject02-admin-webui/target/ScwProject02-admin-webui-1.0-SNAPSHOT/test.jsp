<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<%@include file="./WEB-INF/include-header.jsp" %>
<body>
<%@include file="./WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="./WEB-INF/include-header.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <%@include file="./WEB-INF/include-main.jsp" %>

            <script>
                $(function () {
                    $("#btn").click(function () {

                        console.log("ajax函数之前")
                        $.ajax({
                            url: "${pageContext.request.contextPath}/test/ajax/async.html",
                            type: "post",
                            dataType: "text",
                            async : false,//将ajax请求变为同步请求
                            contentType: "application/json;charset=UTF-8",
                            success: function (data) {
                                console.log(data);
                            },
                            error: function () {
                                console.log("请求发送错误");
                            }
                        });

                        console.log("ajax函数之后")

                        //     在ajax之后执行
                       /* setTimeout(function () {
                            console.log("ajax函数之后")

                        }, 5000);*/
                    })
                })
            </script>

            <button id="btn">请求发送</button>
        </div>
    </div>
</div>

</body>
</html>

