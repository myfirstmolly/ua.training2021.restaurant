<?xml version="1.0" encoding="UTF-8"?>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="m" uri="http://tomcat.apache.org/tags" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="status" var="status"/>
<fmt:message key="quantity" var="quantity"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="total" var="total"/>
<fmt:message key="update.status" var="updateStatus"/>
<fmt:message key="save" var="save"/>

<html>

<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/order.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="order_container">
    <c:choose>
        <c:when test="${sessionScope.lang eq 'ukr'}">
            <h2>${status}: ${request.status.nameUkr}</h2>
        </c:when>
        <c:otherwise>
            <h2>${status}: ${request.status.name}</h2>
        </c:otherwise>
    </c:choose>

    <c:forEach var="requestItem" items="${requestItems}">
        <div class="dish">
            <div class="menu_img">
                <img src="${requestItem.dish.imagePath}">
            </div>
            <div class="details">
                <a href="api?command=dish&id=${requestItem.dish.id}">
                    <c:choose>
                        <c:when test="${sessionScope.lang eq 'ukr'}">
                            <h2>${requestItem.dish.nameUkr}</h2>
                        </c:when>
                        <c:otherwise>
                            <h2>${requestItem.dish.name}</h2>
                        </c:otherwise>
                    </c:choose>
                </a>
                <b>${quantity}: </b>
                <b id="counter"> ${requestItem.quantity} </b>
            </div>
            <div class="price">
                <h3 class="item_price"><m:priceFormat price="${requestItem.price}"/></h3>
                <h3 class="currency">${currency}</h3>
            </div>
        </div>
    </c:forEach>
    <div class="total">
        <div>
            <b id="total">${total}:</b>
            <b id="sum">${request.totalPrice/100}</b>
            <b>uah</b>
        </div>
    </div>
</div>
<c:if test="${role.name == 'MANAGER'}">
    <form action="api" method="POST">
        <input type="hidden" name="command" value="updateStatus">
        <input type="hidden" name="id" value="${request.id}">
        <p>${updateStatus}:</p>
        <select name="status">
            <c:choose>
                <c:when test="${sessionScope.lang eq 'ukr'}">
                    <option selected value="${request.status.id}">${request.status.nameUkr}</option>
                </c:when>
                <c:otherwise>
                    <option selected value="${request.status.id}">${request.status.name}</option>
                </c:otherwise>
            </c:choose>
            <c:forEach var="status" items="${statusList}">
                <c:choose>
                    <c:when test="${sessionScope.lang eq 'ukr'}">
                        <option value="${status.id}">${status.nameUkr}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${status.id}">${status.name}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
        <button type="submit">${save}</button>
    </form>
</c:if>
</body>
</html>
