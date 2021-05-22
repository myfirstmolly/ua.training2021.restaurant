<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
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
    <title>${title}</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
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
                    <a href="api?command=menu&orderBy=name#menu">${name}</a>
                </div>
            </div>
            <div class="dropdown">
                <button class="dropbtn">${category}</button>
                <div class="dropdown-content">
                    <a href="api?command=menu&category=all#menu">${all}</a>
                    <c:forEach var="category" items="${categories}">
                        <a href="api?command=menu&category=${category.id}#menu">${category.name}</a>
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
                    <c:choose>
                        <c:when test="${sessionScope.lang eq 'ukr'}">
                            <a href="api?command=dish&id=${dish.id}">
                                <h3>${dish.nameUkr}</h3>
                            </a>
                            <p>${dish.descriptionUkr}</p>
                        </c:when>
                        <c:otherwise>
                            <a href="api?command=dish&id=${dish.id}">
                                <h3>${dish.name}</h3>
                            </a>
                            <p>${dish.description}</p>
                        </c:otherwise>
                    </c:choose>
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