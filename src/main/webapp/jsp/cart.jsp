<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="i18n"/>

<fmt:message key="title" var="title"/>
<fmt:message key="quantity" var="quantity"/>
<fmt:message key="total" var="total"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="checkout" var="checkout"/>

<html>

<head>
    <title>${title}</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/cart.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navigation.jsp"/>
<div class="cart_container">
    <c:forEach var="requestItem" items="${requestItems}">
        <div class="dish">
            <form method="POST" action="api">
                <input type="hidden" name="command" value="deleteRequestItem">
                <input type="hidden" name="id" value="${requestItem.id}">
                <div class="menu_img">
                    <button type="submit" class="delete_icon">X</button>
                    <img src="${requestItem.dish.imagePath}">
                </div>
            </form>
            <div class="details">
                <a href="api?command=dish&id=${requestItem.dish.id}">
                    <c:choose>
                        <c:when test="${requestScope.lang eq 'ukr'}">
                            <h2>${requestItem.dish.nameUkr}</h2>
                        </c:when>
                        <c:otherwise>
                            <h2>${requestItem.dish.name}</h2>
                        </c:otherwise>
                    </c:choose>
                </a>
                <b>${quantity}: </b>
                <span class="counter">
                    <a href="api?command=updateQty&action=decrease&id=${requestItem.id}">
                        <b class="change_button" id="minus"> - </b>
                    </a>
                    <b id="counter"> ${requestItem.quantity} </b>
                    <a href="api?command=updateQty&action=increase&id=${requestItem.id}">
                        <b class="change_button" id="plus"> + </b>
                    </a>
                </span>
            </div>
            <div class="price">
                <h3 class="item_price">${requestItem.price/100}</h3>
                <h3 class="currency">${currency}</h3>
            </div>
        </div>
    </c:forEach>
    <div class="total">
        <div>
            <b id="total">${total}:</b>
            <b id="sum">${request.totalPrice/100}</b>
            <b>${currency}</b>
        </div>
        <form method="POST" action="api" class="checkout">
            <input type="hidden" name="command" value="checkoutForm">
            <button type="submit">${checkout}</button>
        </form>
    </div>
</div>
</body>

</html>
