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
            <li><a href="api?command=menu#menu">Menu</a></li>
            <c:if test="${isUser}">
                <li><a href="api?command=cart">Cart</a></li>
            </c:if>
            <c:if test="${isAdmin}">
                <li><a href="api?command=addDish">Add dish</a></li>
                <li><a href="api?command=orders">Orders</a></li>
            </c:if>
        </ul>
    </nav>
    <a href="${pageContext.request.contextPath}/jsp/login.jsp">
        <button>Login</button>
    </a>
    <a href="api?command=logout" hidden>
        <button>Logout</button>
    </a>
</header>
<div class="content">
    <div class="dish">
        <div class="menu_img">
            <img src="${pageContext.request.contextPath}/images/${dish.imagePath}">
        </div>
        <div class="dish_description">
            <h3>${dish.name}</h3>
            <div class="desc">
                <p>${dish.description}</p>
            </div>
            <h4>${dish.price/100}</h4>
            <form method="POST" action="api">
                <input type="hidden" name="command" value="addToCart">
                <input type="hidden" value="${dish.id}">
                <button type="submit">Add to cart</button>
            </form>
            <form method="POST" action="api" hidden>
                <input type="hidden" name="command" value="deleteDish">
                <input type="hidden" value="${dish.id}">
                <button>Delete</button>
            </form>
        </div>
    </div>
</div>
</body>

</html>
