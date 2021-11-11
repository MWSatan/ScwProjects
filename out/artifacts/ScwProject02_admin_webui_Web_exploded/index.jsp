<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"
%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <%--    任选其一即可--%>
    <%--    <base href="http://${pageContext.request.serverName } : ${pageContext.request.serverPort }${pageContext.request.contextPath } / "/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Title</title>
    <script src="jquery/jquery-2.1.1.min.js" type="text/javascript"></script>
    <script src="layer/layer.js"></script>
</head>

<script type="text/javascript">

    $(function () {

        $("#btn2").click(function () {

            //准备要发送的数据
            var student = {
                "stuId": 5,
                "stuName": "tom",
                "address": {
                    "province": "江西",
                    "city": "石城",
                    "street": "清华大道",
                },
                "subjectList": [
                    {
                        "subjectName": "javaSE",
                        "subjectScore": 100
                    },
                    {
                        "subjectName": "SSM",
                        "subjectScore": 99
                    }
                ],
                "map": {
                    "k1": "v1",
                    "k2": "v2"
                }
            }

            var requestStudent = JSON.stringify(student);

            $.ajax({
                url: "${pageContext.request.contextPath}/test/testReceive.json",
                type: "post",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                data: requestStudent,
                success: function (data) {
                    console.log(data);
                },
                error: function (data) {
                    console.log(data)
                }
            })
        });


        $("#btn1").click(function () {
            var array = [5, 8, 12];
            var arrayStr = JSON.stringify(array);

            $.ajax({
                url: "${pageContext.request.contextPath}/test/testAjax.html",
                type: "post",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                data: arrayStr,
                success: function (data) {
                    console.log(data)
                },
                error: function (data) {
                    console.log(data)
                }
            })
        })

        $("#btn3").click(function () {

            $.ajax({
                url: "${pageContext.request.contextPath}/test/testForm.html",
                type: "post",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                data: "data",
                success: function (data) {
                    console.log(data)
                },
                error: function (data) {
                    console.log(data)
                }
            })
        })

        $("#layerBtn").click(function () {
             layer.msg("你好")
        })
    })


</script>
<body>
<a href="${pageContext.request.contextPath}/test/testServlet.html">测试SSM整合环境</a>

<br>
<br>

<button id="btn1">send [5,8,12]</button>

<br>
<br>

<button id="btn2">复杂的json数据</button>
<br>
<br>
<button id="btn3">发送ajax请求</button>
<br>
<br>
<button id="layerBtn">layer弹窗</button>
</body>
</html>
