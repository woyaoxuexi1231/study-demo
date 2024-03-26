<%--
  Created by IntelliJ IDEA.
  User: h1123
  Date: 2024/3/26
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit User</title>
</head>
<body>
<h2>Edit User</h2>
<form action="<c:url value='/users/edit/${user.id}'/>" method="post">
    Name: <input type="text" name="name" value="${user.name}"><br>
    <input type="submit" value="Update">
</form>
<br>
<a href="<c:url value='/users/list'/>">Back to User List</a>
</body>
</html>

