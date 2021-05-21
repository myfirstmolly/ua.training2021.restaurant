<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8"%>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <title>Restaurant - the best in the world</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/order.css" rel="stylesheet">
</head>
<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu#menu">Menu</a></li>
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
    <a href="api?command=logout">
        <button>Logout</button>
    </a>
</header>
<div class="order_container">
    <h2>Status: ${request.status}</h2>
    <c:forEach var="requestItem" items="${request.requestItems}">
        <div class="dish">
            <div class="menu_img">
                <img src="${requestItem.dish.imagePath}">
            </div>
            <div class="details">
                <a href="api?command=dish&id=${requestItem.dish.id}">
                    <h2>${requestItem.dish.name}</h2>
                </a>
                <b>Quantity: </b>
                <b id="counter"> ${requestItem.quantity} </b>
            </div>
            <div class="price">
                <h3 class="item_price">${requestItem.price/100}</h3>
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
    </div>
</div>
<c:if test="${role.name == 'MANAGER'}">
    <form action="api" method="POST">
        <input type="hidden" name="command" value="updateStatus">
        <input type="hidden" name="id" value="${request.id}">
        <p>Update status:</p>
        <select name="status">
            <option selected value="${request.status}">${request.status}</option>
            <c:forEach var="status" items="${statusList}">
                <option value="${status}">${status}</option>
            </c:forEach>
        </select>
        <button type="submit">Save</button>
    </form>
</c:if>
</body>
</html>
