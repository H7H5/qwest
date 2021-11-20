<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 09.11.2021
  Time: 21:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>purchares</title>
</head>
<body>
<h2>Розділи</h2>
<table>
    <tr>
        <th>code</th>
        <th>name</th>
    </tr>
    <c:forEach var="purchare" items="${purchareslist}">
        <tr>
            <td><a href="/group/${purchare.code}">${purchare.code}</a></td>
            <td>${purchare.name}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>