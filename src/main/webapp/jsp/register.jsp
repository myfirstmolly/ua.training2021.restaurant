<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8"%>
<html>

<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <title>Restaurant - the best in the world</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/login.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="${pageContext.request.contextPath}/api?command=menu#menu">Menu</a></li>
        </ul>
    </nav>
    <a href="${pageContext.request.contextPath}/jsp/login.jsp"><button>Login</button></a>
</header>
<div class="content">
    <h2>Please, enter<br>your credentials...</h2>
    <form method="POST" action="${pageContext.request.contextPath}/api">
        <input type="hidden" name="command" value="register">
        <input type="text" name="name" class="input_field" placeholder="John Doe">
        <p class="warning">${nameMsg}</p>
        <input type="email" name="email" class="input_field" placeholder="johnDoe@gmail.com">
        <p class="warning">${emailMsg}</p>
        <input type="tel" name="phone" class="input_field" placeholder="(067)123-45-67">
        <p class="warning">${phoneMsg}</p>
        <input type="text" name="username" class="input_field" placeholder="username">
        <p class="warning">${usernameMsg}</p>
        <input type="password" name="password" class="input_field" placeholder="password">
        <p class="warning">${passwordMsg}</p>
        <input class="button" type="submit" value="Register">
    </form>
</div>
</body>

</html>
