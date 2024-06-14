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
  <title>Add User</title>
</head>
<body>
<h2>Add User</h2>
<form action="<c:url value='/users/add'/>" method="post">
  Name: <input type="text" name="name"><br>
  <input type="submit" value="Add">
</form>
<br>
<a href="<c:url value='/users/list'/>">Back to User List</a>
</body>
</html>

