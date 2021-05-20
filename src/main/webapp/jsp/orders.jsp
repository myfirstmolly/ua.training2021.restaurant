<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <title>Restaurant - the best in the world</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/orders.css" rel="stylesheet">
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
    <a href="api?command=logout">
        <button>Logout</button>
    </a>
</header>
<div class="content">
    <div class="menu">
        <div class="options">
            <div class="dropdown">
                <button class="dropbtn">Select order status:</button>
                <div class="dropdown-content">
                    <c:forEach var="status" items="${statusList}">
                        <a href="api?command=orders&status=${status}">
                            <c:out value="${status}"/>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
        <c:choose>
            <c:when test="${fn: length(orders.content) > 0}">
                <table class="orders">
                    <tr>
                        <th>Order No</th>
                        <c:if test="${role.name == 'MANAGER'}">
                            <th>Username</th>
                            <th>Phone number</th>
                        </c:if>
                        <th>Delivery address</th>
                        <th>Status</th>
                        <th>Total price</th>
                    </tr>
                    <c:forEach var="order" items="${orders.content}">
                        <tr>
                            <td>
                                <a href="api?command=order&id=${order.id}">#${order.id}</a>
                            </td>
                            <c:if test="${role.name == 'MANAGER'}">
                                <td>${order.customer.username}</td>
                                <td>${order.customer.phoneNumber}</td>
                            </c:if>
                            <td>${order.deliveryAddress}</td>
                            <td>${order.status}</td>
                            <td>${order.totalPrice/100} uah</td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="pagination">
                    <a href="#">&laquo;</a>
                    <c:forEach var="i" begin="1" end="${orders.totalPages}">
                        <c:choose>
                            <c:when test="${orders.pageIndex == i}">
                                <a class="active" href="api?command=orders&page=${i}"><c:out value="${i}"/></a>
                            </c:when>
                            <c:otherwise>
                                <a href="api?command=orders&page=${i}"><c:out value="${i}"/></a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <a href="#">&raquo;</a>
                </div>
            </c:when>
            <c:otherwise><h2 style="color: white">There are no orders...</h2></c:otherwise>
        </c:choose>
    </div>
</div>
</body>

</html>
