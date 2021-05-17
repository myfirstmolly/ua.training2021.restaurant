<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <li><a href="api?command=menu#menu">Menu</a></li>
            <li><a href="api?command=addDish">Add dish</a></li>
            <li><a href="api?command=orders">Orders</a></li>
        </ul>
    </nav>
    <a href="api?command=logout">
        <button>Logout</button>
    </a>
</header>
<div class="content">
    <a name="menu"></a>
    <div class="menu">
        <div class="options">
            <div class="dropdown">
                <button class="dropbtn">Select order status:</button>
                <div class="dropdown-content">
                    <c:forEach var="status" items="${statusList}">
                        <a href="api?command=orders?status=${status}">
                            <c:out value="${status}"/>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
        <table class="orders">
            <tr>
                <th>Order No</th>
                <th>Username</th>
                <th>Phone number</th>
                <th>Order</th>
                <th>Status</th>
                <th>Total price</th>
            </tr>
            <c:forEach var="order" items="${orders.content}">
                <tr>
                    <td>
                        <a href="api?command=updateStatus?id=${order.id}">${order.id}</a>
                    </td>
                    <td>${order.user.username}</td>
                    <td>${order.user.phoneNumber}</td>
                    <td>${order.orderItems}</td>
                    <td>${order.status}Cooking</td>
                    <td>${order.totalPrice/100} uah</td>
                </tr>
            </c:forEach>
        </table>
        <div class="pagination">
            <a href="#">&laquo;</a>
            <c:forEach var="i" begin="1" end="${orders.totalPages}">
                <c:when test="${orders.pageIndex == i}">
                    <a class="active" href="api?command=orders?page=${i}"><c:out value="${i}"/></a>
                </c:when>
                <c:otherwise>
                    <a href="api?command=orders?page=${i}"><c:out value="${i}"/></a>
                </c:otherwise>
            </c:forEach>
            <a href="#">&raquo;</a>
        </div>
    </div>
</div>
</body>

</html>
