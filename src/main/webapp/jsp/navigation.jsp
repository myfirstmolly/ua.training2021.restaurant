<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="menu" var="menu"/>
<fmt:message key="cart" var="cart"/>
<fmt:message key="my.orders" var="myOrders"/>
<fmt:message key="add.dish" var="addDish"/>
<fmt:message key="orders" var="orders"/>
<fmt:message key="language" var="lang"/>
<fmt:message key="language.code" var="langCode"/>
<fmt:message key="login" var="login"/>
<fmt:message key="logout" var="logout"/>

<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">${menu}</a></li>
            <c:if test="${sessionScope.user.role.name == 'CUSTOMER'}">
                <li><a href="api?command=cart">${cart}</a></li>
                <li><a href="api?command=orders">${myOrders}</a></li>
            </c:if>
            <c:if test="${sessionScope.user.role.name == 'MANAGER'}">
                <li><a href="api?command=addDishGetPage">${addDish}</a></li>
                <li><a href="api?command=orders">${orders}</a></li>
            </c:if>
            <li><a href="api?command=setLocale&lang=${langCode}">${lang}</a></li>
        </ul>
    </nav>
    <c:choose>
        <c:when test="${sessionScope.user.role == null}">
            <a href="/restaurant">
                <button>${login}</button>
            </a>
        </c:when>
        <c:otherwise>
            <a href="api?command=logout">
                <button>${logout}</button>
            </a>
        </c:otherwise>
    </c:choose>
</header>