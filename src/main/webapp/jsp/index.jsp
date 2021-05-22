<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="resources"/>
<fmt:setLocale value="ukr"/>
<fmt:message key="menu" var="menu"/>
<fmt:message key="cart" var="cart"/>
<fmt:message key="my.orders" var="myOrders"/>
<fmt:message key="add.dish" var="addDish"/>
<fmt:message key="orders" var="orders"/>
<fmt:message key="language" var="lang"/>
<fmt:message key="language.code" var="langCode"/>
<fmt:message key="login" var="login"/>
<fmt:message key="logout" var="logout"/>
<fmt:message key="restaurant.name" var="restaurantName"/>
<fmt:message key="restaurant.desc" var="desc"/>
<fmt:message key="order.by" var="orderBy"/>
<fmt:message key="price" var="price"/>
<fmt:message key="category" var="category"/>
<fmt:message key="name" var="name"/>
<fmt:message key="all" var="all"/>
<fmt:message key="currency" var="currency"/>
<html>

<head>
    <title>Restaurant - the best in the world</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">${menu}</a></li>
            <c:if test="${role.name == 'CUSTOMER'}">
                <li><a href="api?command=cart">${cart}</a></li>
                <li><a href="api?command=orders">${myOrders}</a></li>
            </c:if>
            <c:if test="${role.name == 'MANAGER'}">
                <li><a href="api?command=addDishGetPage">${addDish}</a></li>
                <li><a href="api?command=orders">${orders}</a></li>
            </c:if>
            <li><a href="api?command=setLocale&lang=${langCode}#menu">${lang}</a></li>
        </ul>
    </nav>
    <c:choose>
        <c:when test="${role == null}">
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
<div class="content">
    <div class="main_banner">
        <h1>${restaurantName}</h1>
        <p>${desc}</p>
    </div>
    <a name="menu"></a>
    <div class="menu">
        <div class="options">
            <div class="dropdown">
                <button class="dropbtn">${orderBy}</button>
                <div class="dropdown-content">
                    <a href="api?command=menu&orderBy=price#menu">${price}</a>
                    <a href="api?command=menu&orderBy=category#menu">${category}</a>
                    <a href="api?command=menu&orderBy=name#menu">${category}</a>
                </div>
            </div>
            <div class="dropdown">
                <button class="dropbtn">${category}</button>
                <div class="dropdown-content">
                    <a href="api?command=menu&category=all#menu">${all}</a>
                    <c:forEach var="category" items="${categories}">
                        <c:choose>
                            <c:when test="${fn:startsWith(sessionScope.locale, 'ukr')}">
                                <a href="api?command=menu&category=${category.id}#menu">${category.nameUkr}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="api?command=menu&category=${category.id}#menu">${category.name}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="menu_items">
            <c:forEach var="dish" items="${dishes.content}">
                <div class="menu_item">
                    <div class="menu_img">
                        <a href="api?command=dish&id=${dish.id}">
                            <img src="${dish.imagePath}">
                        </a>
                    </div>
                    <a href="api?command=dish&id=${dish.id}">
                        <h3>${dish.name}</h3>
                    </a>
                    <p>${dish.description}</p>
                    <h4>${dish.price/100} ${currency}</h4>
                </div>
            </c:forEach>
        </div>
        <c:if test="${dishes.totalPages > 1}">
            <div class="pagination">
                <c:forEach var="i" begin="1" end="${dishes.totalPages}">
                    <c:choose>
                        <c:when test="${dishes.pageIndex == i}">
                            <a class="active" href="api?command=menu&page=${i}#menu"><c:out value="${i}"/></a>
                        </c:when>
                        <c:otherwise>
                            <a href="api?command=menu&page=${i}#menu"><c:out value="${i}"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>
    </div>
</div>
</body>

</html>