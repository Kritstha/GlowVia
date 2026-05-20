<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    This checks if there is an error message in the session
    If yes it shows a red toast message and removes it from session
-->
<c:if test="${not empty sessionScope.error}">
    <div class="toast toast-error">${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
</c:if>

<!--
    This checks if there is a success message in the session
    If yes it shows a green toast message and removes it from session
-->
<c:if test="${not empty sessionScope.success}">
    <div class="toast toast-success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
</c:if>

<!--
    This is the main registration section of the page
    It shows the registration form in the center of the page
-->
<main class="auth-shell">
    <div class="auth-card auth-card-wide">

        <!--
            This is the registration page heading
            It shows the title and a short welcome message
        -->
        <h1>Create your account</h1>
        <p class="muted">Join the Glowvia family - it only takes a minute.</p>

        <!--
            This is the registration form
            Customer fills in all their details to create a new account
            The form uses multipart so the customer can also upload a profile image
        -->
        <form method="post" action="${pageContext.request.contextPath}/register"
              enctype="multipart/form-data" class="form form-grid">

            <!--
                This is the full name input field
                Customer must enter their full name to register
            -->
            <div class="form-row">
                <label for="fullName">Full Name</label>
                <input id="fullName" name="fullName" type="text" required maxlength="100"/>
            </div>

            <!--
                This is the username input field
                Username must be at least 5 characters and maximum 30 characters
            -->
            <div class="form-row">
                <label for="username">Username</label>
                <input id="username" name="username" type="text" required
                       minlength="5" maxlength="30"/>
            </div>

            <!--
                This is the email input field
                Customer must enter a valid email address to register
            -->
            <div class="form-row">
                <label for="email">Email Address</label>
                <input id="email" name="email" type="email" required maxlength="100"/>
            </div>

            <!--
                This is the phone number input field
                Customer must enter a 10 digit phone number to register
            -->
            <div class="form-row">
                <label for="phone">Phone Number</label>
                <input id="phone" name="phone" type="tel" required maxlength="10"/>
            </div>

            <!--
                This is the date of birth input field
                This field is optional and customer can leave it empty
            -->
            <div class="form-row">
                <label for="dob">Date of Birth</label>
                <input id="dob" name="dob" type="date"/>
            </div>

            <!--
                This is the gender dropdown field
                Customer can select their gender or choose prefer not to say
            -->
            <div class="form-row">
                <label for="gender">Gender</label>
                <select id="gender" name="gender">
                    <option value="">Prefer not to say</option>
                    <option value="Female">Female</option>
                    <option value="Male">Male</option>
                    <option value="Other">Other</option>
                </select>
            </div>

            <!--
                This is the password input field
                Password must be at least 6 characters long
            -->
            <div class="form-row">
                <label for="password">Password</label>
                <input id="password" name="password" type="password"
                       required minlength="6"/>
            </div>

            <!--
                This is the confirm password input field
                Customer must type the password again to make sure it matches
            -->
            <div class="form-row">
                <label for="confirmPassword">Confirm Password</label>
                <input id="confirmPassword" name="confirmPassword" type="password"
                       required minlength="6"/>
            </div>

            <!--
                This is the profile image upload field
                This field is optional and customer can leave it empty
                If no image is uploaded a default image will be used
            -->
            <div class="form-row form-row-full">
                <label for="image">Profile Image (optional)</label>
                <input id="image" name="image" type="file" accept="image/*"/>
            </div>

            <!--
                This is the create account button
                Clicking this submits the form and creates the new account
            -->
            <div class="form-row form-row-full">
                <button type="submit" class="btn btn-primary btn-block">
                    Create Account
                </button>
            </div>
        </form>

        <!--
            This shows a link to the login page
            If the customer already has an account they can click here to sign in
        -->
        <p class="form-foot muted">
            Already a member?
            <a href="${pageContext.request.contextPath}/login">Sign in instead</a>
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