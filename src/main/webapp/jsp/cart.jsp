<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Restaurant - the best in the world</title>
    <meta charset="UTF-8">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/cart.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">Menu</a></li>
            <li><a href="api?command=cart">Cart</a></li>
        </ul>
    </nav>
    <a href="api?command=logout">
        <button>Logout</button>
    </a>
</header>
<div class="cart_container">
    <c:forEach var="requestItem" items="${request.requestItems}">
        <div class="dish">
            <form method="POST" action="api">
                <input type="hidden" name="command" value="deleteRequestItem">
                <input type="hidden" name="id" value="${requestItem.id}">
                <div class="menu_img">
                    <button type="submit" class="delete_icon">X</button>
                    <img src="${pageContext.request.contextPath}/images/${requestItem.dish.imagePath}">
                </div>
            </form>
            <div class="details">
                <h2>${requestItem.dish.name}</h2>
                <b>Quantity: </b>
                <span class="counter">
                    <a href="api?command=updateQty?update=decrease?id=${requestItem.id}">
                        <b class="change_button" id="minus"> - </b>
                    </a>
                    <b id="counter"> ${requestItem.quantity} </b>
                    <a href="api?command=updateQty?update=increase?id=${requestItem.id}">
                        <b class="change_button" id="plus"> + </b>
                    </a>
                </span>
            </div>
            <div class="price">
                <h3 class="item_price">${requestItem.price}</h3>
                <h3 class="currency">uah</h3>
            </div>
        </div>
    </c:forEach>
    <div class="total">
        <div>
            <b id="total">Total:</b>
            <b id="sum">${request.totalPrice/100}</b>
            <b>uah</b>
        </div>
        <form method="POST" action="api" class="checkout">
            <input type="hidden" name="command" value="checkout">
            <button type="submit">Checkout</button>
        </form>
    </div>
</div>
</body>

</html>
