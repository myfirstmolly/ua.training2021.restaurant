<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8"%>
<html>
<head>
    <title>Restaurant - the best in the world</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/checkout.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">Menu</a></li>
            <li><a href="api?command=addDishGetPage">Add dish</a></li>
            <li><a href="api?command=orders">Orders</a></li>
        </ul>
    </nav>
    <a href="api?command=logout">
        <button>Logout</button>
    </a>
</header>
<div class="content">
    <h1>Checkout</h1>
    <form class="delivery" action="api" method="POST">
        <input type="hidden" name="command" value="checkout">
        <textarea name="address" rows="3" placeholder="My address..."></textarea>
        <p class="warning">${addressMsg}</p>
        <b class="total">Total:
            <b id="sum">${totalPrice/100}</b>
        </b>
        <button type="submit">Submit</button>
    </form>
</div>
</body>
</html>
