<?xml version="1.0" encoding="UTF-8"?>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<%@page isErrorPage = "true" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>

<html>

<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="content">
    <div class="main_banner">
        <h1>404</h1>
        <p>${pageContext.exception.message}</p>
    </div>
</div>

</body>
</html>
