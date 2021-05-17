<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>

<head>
    <title>Restaurant - the best in the world</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">Menu</a></li>
            <c:if test="${role.name == 'CUSTOMER'}">
                <li><a href="api?command=cart">Cart</a></li>
            </c:if>
            <c:if test="${role.name == 'MANAGER'}">
                <li><a href="api?command=addDish">Add dish</a></li>
                <li><a href="api?command=orders">Orders</a></li>
            </c:if>
        </ul>
    </nav>
    <c:choose>
        <c:when test="${role == null}">
            <a href="/restaurant">
                <button>Login</button>
            </a>
        </c:when>
        <c:otherwise>
            <a href="api?command=logout">
                <button>Logout</button>
            </a>
        </c:otherwise>
    </c:choose>
</header>
<div class="content">
    <div class="main_banner">
        <h1>Restaurant</h1>
        <p>The best food in the world</p>
    </div>
    <a name="menu"></a>
    <div class="menu">
        <div class="options">
            <div class="dropdown">
                <button class="dropbtn">Order by</button>
                <div class="dropdown-content">
                    <a href="api?command=menu?orderBy=price">Price</a>
                    <a href="api?command=menu?orderBy=category">Category</a>
                    <a href="api?command=menu?orderBy=name">Name</a>
                </div>
            </div>
            <div class="dropdown">
                <button class="dropbtn">Category</button>
                <div class="dropdown-content">
                    <c:forEach var="category" items="${categories}">
                        <a href="api?command=menu&category=${category.id}">${category.name}</a>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="menu_items">
            <c:forEach var="dish" items="${dishes.content}">
                <div class="menu_item">
                    <div class="menu_img">
                        <a href="api?command=dish?id=${dish.id}">
                            <img src="${pageContext.request.contextPath}/images/${dish.imagePath}">
                        </a>
                    </div>
                    <a href="api?command=dish?id=${dish.id}">
                        <h3>${dish.name}</h3>
                    </a>
                    <p>${dish.description}</p>
                    <h4>${dish.price/100} uah</h4>
                </div>
            </c:forEach>
        </div>
        <c:if test="${dishes.totalPages > 1}">
            <div class="pagination">
                <a href="#">&laquo;</a>
                <c:forEach var="i" begin="1" end="${dishes.totalPages}">
                    <c:choose>
                        <c:when test="${dishes.pageIndex == i}">
                            <a class="active" href="api?command=menu?page=${i}"><c:out value="${i}"/></a>
                        </c:when>
                        <c:otherwise>
                            <a href="api?command=menu?page=${i}"><c:out value="${i}"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <a href="#">&raquo;</a>
            </div>
        </c:if>
    </div>
</div>
</body>

</html>