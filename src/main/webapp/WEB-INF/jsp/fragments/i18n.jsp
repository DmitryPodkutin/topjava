<%--https://coderoad.ru/13865245/Передача-параметров-JSP-от-одного-jsp-к-другому-код-использует-HIPPO--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    const i18n = [];
    i18n["addTitle"] = '<spring:message code="${param.text}.add"/>';
    i18n["editTitle"] = '<spring:message code="${param.text}.edit"/>';
    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"}%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>