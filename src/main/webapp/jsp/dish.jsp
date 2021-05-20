<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <title>Restaurant - the best in the world</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/dish.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu&dropCookies=true#menu">Menu</a></li>
            <c:if test="${role.name == 'CUSTOMER'}">
                <li><a href="api?command=cart">Cart</a></li>
                <li><a href="api?command=orders">My Orders</a></li>
            </c:if>
            <c:if test="${role.name == 'MANAGER'}">
                <li><a href="api?command=addDishGetPage">Add dish</a></li>
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
    <div class="dish">
        <div class="menu_img">
            <img src="${dish.imagePath}">
        </div>
        <div class="dish_description">
            <h3>${dish.name}</h3>
            <div class="desc">
                <p>${dish.description}</p>
            </div>
            <h4>${dish.price/100}</h4>
            <c:if test="${role.name == 'CUSTOMER'}">
                <form method="POST" action="api">
                    <input type="hidden" name="command" value="addToCart">
                    <input type="hidden" name="dish" value="${dish.id}">
                    <button type="submit">Add to cart</button>
                </form>
            </c:if>
            <c:if test="${role.name == 'MANAGER'}">
                <form method="POST" action="api">
                    <input type="hidden" name="command" value="deleteDish">
                    <input type="hidden" name="dish" value="${dish.id}">
                    <button>Delete</button>
                </form>
            </c:if>
        </div>
    </div>
</div>
</body>

</html>
