<%--
  Created by IntelliJ IDEA.
  User: h1123
  Date: 2024/3/26
  Time: 23:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
</head>
<body>
<h2>User List</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Actions</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>
                <a href="<c:url value='/users/edit/${user.id}'/>">Edit</a>
                <a href="<c:url value='/users/delete/${user.id}'/>">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="<c:url value='/users/add'/>">Add User</a>

<p><a href="${pageContext.request.contextPath}">Back to Home</a></p>
</body>
</html>


