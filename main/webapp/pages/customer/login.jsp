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


<jsp:include page="/includes/customer_nav.jsp" />


<jsp:include page="/includes/flash.jsp" />


<main class="auth-shell">
    <div class="auth-card">


        <h1>Welcome back</h1>
        <p class="muted">Sign in to continue your skincare journey.</p>


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

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>