<%--
  Created by IntelliJ IDEA.
  User: h1123
  Date: 2022/11/16
  Time: 22:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-3.6.4.min.js"></script>
</head>
<body>
<a href="mvc?name=hello,world">点我</a>
<p>${data}</p>
<a href="javascript:void(0)" id="returnJson" onclick="getSimpleJson()">点我</a>
<script type="text/javascript">
    function getSimpleJson() {
        var url = "/simpleJS";
        var args = {};
        $.post(url, args, function (data) {
            console.log(data);
        })
    }
</script>
<form>

</form>
</body>
</html>
