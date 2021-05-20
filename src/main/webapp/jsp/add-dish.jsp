<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <title>Restaurant - the best in the world</title>
    <meta charset="UTF-8">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/add-dish.css" rel="stylesheet">
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
    <h1>Add new dish</h1>
    <form class="dish" action="api" method="POST">
        <input type="hidden" name="command" value="addDish">
        <input class="text" type="text" name="name" placeholder="Dish name...">
        <p class="warning">${nameMsg}</p>
        <input class="text" type="text" name="imagePath" placeholder="google.com/image-link/">
        <p class="warning">${imageMsg}</p>
        <textarea name="description" rows="20" placeholder="Dish description"></textarea>
        <b>Price
            <input class="price" type="text" name="price" placeholder="80.00">
        </b>
        <p class="warning">${priceMsg}</p>
        <b>Category
            <select name="category">
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>
        </b>
        <button type="submit">Save</button>
    </form>
</div>
</body>

</html>
