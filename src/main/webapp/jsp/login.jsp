<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>

<!-- Messaggio di errore eventualmente passato dal controller -->
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>


<div class="login-container">
    <h2>Login</h2>

    <!-- Form di login -->
    <form method="post" action="login">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required autofocus>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Accedi</button>
    </form>
</div>

</body>
</html>
