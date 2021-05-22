<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="add.to.cart" var="addToCart"/>
<fmt:message key="delete" var="delete"/>

<html>

<head>
    <title>${title}</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/dish.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="content">
    <div class="dish">
        <div class="menu_img">
            <img src="${dish.imagePath}">
        </div>
        <div class="dish_description">
            <c:choose>
                <c:when test="${sessionScope.lang eq 'ukr'}">
                    <h3>${dish.nameUkr}</h3>
                    <div class="desc">
                        <p>${dish.descriptionUkr}</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <h3>${dish.name}</h3>
                    <div class="desc">
                        <p>${dish.description}</p>
                    </div>
                </c:otherwise>
            </c:choose>
            <h4>${dish.price/100} ${currency}</h4>
            <c:if test="${role.name == 'CUSTOMER'}">
                <form method="POST" action="api">
                    <input type="hidden" name="command" value="addToCart">
                    <input type="hidden" name="dish" value="${dish.id}">
                    <button type="submit">${addToCart}</button>
                </form>
            </c:if>
            <c:if test="${role.name == 'MANAGER'}">
                <form method="POST" action="api">
                    <input type="hidden" name="command" value="deleteDish">
                    <input type="hidden" name="dish" value="${dish.id}">
                    <button>${delete}</button>
                </form>
            </c:if>
        </div>
    </div>
</div>
</body>

</html>
