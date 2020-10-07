<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Add/Edit Meal</title>
</head>
<body>
<h2>Save/Edit Meal</h2>
<form method="POST" action='meals' name="addMeal">
    <input type="hidden" name="id"
           value="<c:out value="${meal.id}" />"/> <br/>
    <javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm" var="dateTime"/>
    Date <input type="datetime-local" name="date" value="${dateTime}"/> <br/>
    Description : <input type="text" name="description" value="${meal.description}"/> <br/>
    Calories <input type="text" name="calories" value="${meal.calories}"/> <br/>
    <input type="submit" value="OK"/> <input type="submit" value="Cancel"/>
</form>
</body>
</html>
