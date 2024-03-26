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
    <script type="text/javascript" src="scripts/jquery-3.6.4.min.js"></script>
</head>
<body>

<p>
    <a href="mvc?name=hello,world">点击跳转到SimpleMVC.jsp</a>
</p>

<p>
    <%--javascript:void(0) 是一个 JavaScript 伪协议,作用是在点击链接时执行一段 JavaScript 代码，并防止浏览器执行默认的跳转行为。--%>
    <%--<a href="javascript:void(0)" id="returnJson" onclick="getSimpleJson()">点我</a>--%>
    <button onclick="getSimpleJson()">点击发送一个post请求,并得到一个字符串结果</button>
</p>
<div id="result"></div>
<script type="text/javascript">
    function getSimpleJson() {
        // 如果您的应用部署在 http://localhost:8080/myapp/，那么 pageContext.request.contextPath 将返回 /myapp
        var url = "${pageContext.request.contextPath}/simpleJS";
        var args = {};
        $.post(url, args, function (data) {
            console.log(data);
            var response = JSON.parse(data);
            var formattedResponse = JSON.stringify(response, null, 2); // 格式化JSON
            document.getElementById('result').innerHTML = '<pre>' + formattedResponse + '</pre>'; // 在<pre>标签中显示格式化后的JSON
        })
    }
</script>
<form>

</form>
</body>
</html>
