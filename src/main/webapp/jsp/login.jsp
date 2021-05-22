<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="menu" var="menu"/>
<fmt:message key="cart" var="cart"/>
<fmt:message key="language" var="lang"/>
<fmt:message key="language.code" var="langCode"/>
<fmt:message key="login" var="login"/>
<fmt:message key="title" var="title"/>
<fmt:message key="enter.credentials.msg" var="credMsg"/>
<fmt:message key="placeholder.login" var="login"/>
<fmt:message key="placeholder.password" var="password"/>
<fmt:message key="login" var="loginBtn"/>
<fmt:message key="register" var="register"/>

<html>

<head>
    <title>${title}</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/login.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">${menu}</a></li>
            <li><a href="api?command=setLocale&lang=${langCode}">${lang}</a></li>
        </ul>
    </nav>
    <a href="${pageContext.request.contextPath}/jsp/register.jsp">
        <button>${register}</button>
    </a>
</header>
<div class="content">
    <h2>${credMsg}</h2>
    <form method="POST" action="${pageContext.request.contextPath}/api">
        <input type="hidden" name="command" value="login">
        <input type="text" name="login" class="input_field" placeholder=${login}>
        <input type="password" name="password" class="input_field" placeholder=${password}>
        <p class="warning">${message}</p>
        <input class="button" type="submit" value=${loginBtn}>
    </form>
</div>
</body>

</html>
