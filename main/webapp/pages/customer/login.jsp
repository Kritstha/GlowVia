<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body auth-body">

<!--
    This includes the customer navigation bar at the top of the page
    It shows the logo, navigation links and login or logout buttons
-->
<jsp:include page="/includes/customer_nav.jsp" />

<!--
    This includes the flash messages section
    It shows success or error messages after an action
-->
<jsp:include page="/includes/flash.jsp" />

<!--
    This is the main login section of the page
    It shows the login form in the center of the page
-->
<main class="auth-shell">
    <div class="auth-card">

        <!--
            This is the login page heading
            It welcomes the user back and shows a short description
        -->
        <h1>Welcome back</h1>
        <p class="muted">Sign in to continue your skincare journey.</p>

        <!--
            This is the login form
            User enters their username or email and password to sign in
            The form submits to the login controller via POST method
        -->
        <form method="post" action="${pageContext.request.contextPath}/login" class="form">

            <!--
                This is the username or email input field
                User can enter either their username or email address to login
            -->
            <div class="form-row">
                <label for="identifier">Username or Email</label>
                <input id="identifier" name="identifier" type="text" required maxlength="100"/>
            </div>

            <!--
                This is the password input field
                User must enter their password which must be at least 6 characters
            -->
            <div class="form-row">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" required minlength="6"/>
            </div>

            <!--
                This is the sign in button
                Clicking this submits the form and logs the user in
            -->
            <button type="submit" class="btn btn-primary btn-block">Sign In</button>
        </form>

        <!--
            This shows a link to the register page
            If the user does not have an account they can click here to create one
        -->
        <p class="form-foot muted">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register">Create one</a>
        </p>
    </div>
</main>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>