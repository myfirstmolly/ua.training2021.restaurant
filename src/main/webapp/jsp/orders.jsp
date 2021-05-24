<?xml version="1.0" encoding="UTF-8"?>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="m" uri="http://tomcat.apache.org/tags" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="select.order.status" var="selectStatus"/>
<fmt:message key="all" var="all"/>
<fmt:message key="order" var="order"/>
<fmt:message key="username" var="username"/>
<fmt:message key="phone.number" var="phoneNumber"/>
<fmt:message key="delivery.address" var="delivery"/>
<fmt:message key="status" var="statusProp"/>
<fmt:message key="total" var="total"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="no.orders" var="noOrders"/>

<html>

<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/orders.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="content">
    <div class="menu">
        <div class="options">
            <div class="dropdown">
                <button class="dropbtn">${selectStatus}:</button>
                <div class="dropdown-content">
                    <a href="api?command=orders&status=ALL">${all}</a>
                    <c:forEach var="status" items="${statusList}">
                        <a href="api?command=orders&status=${status}">
                            <c:choose>
                                <c:when test="${sessionScope.lang eq 'ukr'}">
                                    <c:out value="${status.nameUkr}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${status.name}"/>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
        <c:choose>
            <c:when test="${fn: length(orders.content) > 0}">
                <table class="orders">
                    <tr>
                        <th>${order} No</th>
                        <c:if test="${role.name == 'MANAGER'}">
                            <th>${username}</th>
                            <th>${phoneNumber}</th>
                        </c:if>
                        <th>${delivery}</th>
                        <th>${statusProp}</th>
                        <th>${total}</th>
                    </tr>
                    <c:forEach var="order" items="${orders.content}">
                        <tr>
                            <td>
                                <a href="api?command=order&id=${order.id}">#${order.id}</a>
                            </td>
                            <c:if test="${role.name == 'MANAGER'}">
                                <td>${order.customer.username}</td>
                                <td>${order.customer.phoneNumber}</td>
                            </c:if>
                            <td>${order.deliveryAddress}</td>
                            <td>${order.status}</td>
                            <td><m:priceFormat price="${order.totalPrice}"/> ${currency}</td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${orders.totalPages > 1}">
                    <div class="pagination">
                        <c:forEach var="i" begin="1" end="${orders.totalPages}">
                            <c:choose>
                                <c:when test="${orders.pageIndex == i}">
                                    <a class="active" href="api?command=orders&page=${i}"><c:out value="${i}"/></a>
                                </c:when>
                                <c:otherwise>
                                    <a href="api?command=orders&page=${i}"><c:out value="${i}"/></a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise><h2 style="color: white">${noOrders}</h2></c:otherwise>
        </c:choose>
    </div>
</div>
</body>

</html>
