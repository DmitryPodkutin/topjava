<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style type="text/css">
        tr.red {
            color: red;
        }

        tr.green {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>MealList</h2>
<table border="0" width="50%" cellpadding="5">
    <tr>
        <th>Id</th>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Action</th>

    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        <tr align="center"  class="${ meal.excess ? 'red' : 'green' }" >
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
                <%--<fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />--%>
                <%--<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${ parsedDateTime }" /></td>--%>
                <%--<td>${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}</td>--%>
            <javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm" var="dateTime"/>
            <td>${meal.id}</td>
            <td>${dateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<a href="meals?action=save">Add Meal</a>
</body>
</html>