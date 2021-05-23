<?xml version="1.0" encoding="UTF-8"?>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="checkout" var="checkout"/>
<fmt:message key="my.address" var="myAddress"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="submit" var="submit"/>

<html>

<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/checkout.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="content">
    <h1>${checkout}</h1>
    <form class="delivery" action="api" method="POST">
        <input type="hidden" name="command" value="checkout">
        <textarea name="address" rows="3" placeholder="${myAddress}"></textarea>
        <p class="warning">${addressMsg}</p>
        <b class="total">Total:
            <b id="sum">${totalPrice/100} ${currency}</b>
        </b>
        <button type="submit">${submit}</button>
    </form>
</div>
</body>
</html>
