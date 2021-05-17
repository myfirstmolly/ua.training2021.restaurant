<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Restaurant - the best in the world</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;900&display=swap" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/style/login.css" rel="stylesheet">
</head>

<body>
<header>
    <nav>
        <ul class="nav_links">
            <li><a href="api?command=menu">Menu</a></li>
        </ul>
    </nav>
    <a href="register.jsp"><button>Register</button></a>
</header>
<div class="content">
    <h2>Please, enter<br>your credentials...</h2>
    <form method="POST" action="api">
        <input type="hidden" name="command" value="login">
        <input type="text" name="login" class="input_field" placeholder="login">
        <input type="password" name="password" class="input_field" placeholder="password">
        <input class="button" type="submit" value="Login">
    </form>
</div>
</body>

</body>
</html>
