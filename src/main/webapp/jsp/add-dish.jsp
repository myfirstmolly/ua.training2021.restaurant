<?xml version="1.0" encoding="UTF-8"?>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="add.dish" var="addDish"/>
<fmt:message key="price" var="price"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="category" var="category"/>
<fmt:message key="save" var="save"/>

<html>

<head>

    <title>${title}</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/add-dish.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="content">
    <h1>${addDish}</h1>
    <form class="dish" action="api" method="POST">
        <input type="hidden" name="command" value="addDish">
        <input class="text" type="text" name="name" placeholder="Dish name...">
        <p class="warning">${nameMsg}</p>
        <input class="text" type="text" name="nameUkr" placeholder="Назва страви...">
        <p class="warning">${nameUkrMsg}</p>
        <input class="text" type="text" name="imagePath" placeholder="google.com/image-link/">
        <p class="warning">${imageMsg}</p>
        <textarea name="description" rows="20" placeholder="Dish description"></textarea>
        <textarea name="descriptionUkr" rows="20" placeholder="Опис страви..."></textarea>
        <b>${price}
            <input class="price" type="text" name="price" placeholder="80.00">
            ${currency}
        </b>
        <p class="warning">${priceMsg}</p>
        <b>${category}
            <select name="category">
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>
        </b>
        <button type="submit">${save}</button>
    </form>
</div>
</body>

</html>
