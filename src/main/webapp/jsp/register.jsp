<?xml version="1.0" encoding="UTF-8"?>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="menu" var="menu"/>
<fmt:message key="language" var="lang"/>
<fmt:message key="language.code" var="langCode"/>
<fmt:message key="login" var="login"/>
<fmt:message key="enter.credentials.msg" var="credentialsMsg"/>
<fmt:message key="register" var="registerBtn"/>

<html>

<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/login.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="${pageContext.request.contextPath}/api?command=menu#menu">${menu}</a></li>
            <li><a href="${pageContext.request.contextPath}/api?command=setLocale&lang=${langCode}">${lang}</a></li>
        </ul>
    </nav>
    <a href="/restaurant">
        <button>${login}</button>
    </a>
</header>
<div class="content">
    <h2>${credentialsMsg}</h2>
    <form method="POST" action="${pageContext.request.contextPath}/api">
        <input type="hidden" name="command" value="register">
        <input type="text" name="name" class="input_field" placeholder="John Doe">
        <p class="warning">${nameMsg}</p>
        <input type="email" name="email" class="input_field" placeholder="johnDoe@gmail.com">
        <p class="warning">${emailMsg}</p>
        <input type="tel" name="phone" class="input_field" placeholder="(067)123-45-67">
        <p class="warning">${phoneMsg}</p>
        <input type="text" name="username" class="input_field" placeholder="username">
        <p class="warning">${usernameMsg}</p>
        <input type="password" name="password" class="input_field" placeholder="password">
        <p class="warning">${passwordMsg}</p>
        <input class="button" type="submit" value="${registerBtn}">
    </form>
</div>
</body>

</html>
