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

<jsp:include page="/includes/customer_nav.jsp" />

<!-- Show error message if registration failed -->
<c:if test="${not empty sessionScope.error}">
    <div class="toast toast-error">${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
</c:if>

<!-- Show success message if any -->
<c:if test="${not empty sessionScope.success}">
    <div class="toast toast-success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
</c:if>

<main class="auth-shell">
    <div class="auth-card auth-card-wide">

        <!-- Registration heading -->
        <h1>Create your account</h1>
        <p class="muted">Join the Glowvia family - it only takes a minute.</p>

        <!-- Registration form -->
        <form method="post" action="${pageContext.request.contextPath}/register"
              enctype="multipart/form-data" class="form form-grid">

            <!-- Full name field -->
            <div class="form-row">
                <label for="fullName">Full Name</label>
                <input id="fullName" name="fullName" type="text" required maxlength="100"/>
            </div>

            <!-- Username field -->
            <div class="form-row">
                <label for="username">Username</label>
                <input id="username" name="username" type="text" required
                       minlength="5" maxlength="30"/>
            </div>

            <!-- Email field -->
            <div class="form-row">
                <label for="email">Email Address</label>
                <input id="email" name="email" type="email" required maxlength="100"/>
            </div>

            <!-- Phone field -->
            <div class="form-row">
                <label for="phone">Phone Number</label>
                <input id="phone" name="phone" type="tel" required maxlength="10"/>
            </div>

            <!-- Date of birth field -->
            <div class="form-row">
                <label for="dob">Date of Birth</label>
                <input id="dob" name="dob" type="date"/>
            </div>

            <!-- Gender field -->
            <div class="form-row">
                <label for="gender">Gender</label>
                <select id="gender" name="gender">
                    <option value="">Prefer not to say</option>
                    <option value="Female">Female</option>
                    <option value="Male">Male</option>
                    <option value="Other">Other</option>
                </select>
            </div>

            <!-- Password field -->
            <div class="form-row">
                <label for="password">Password</label>
                <input id="password" name="password" type="password"
                       required minlength="6"/>
            </div>

            <!-- Confirm password field -->
            <div class="form-row">
                <label for="confirmPassword">Confirm Password</label>
                <input id="confirmPassword" name="confirmPassword" type="password"
                       required minlength="6"/>
            </div>


            <!-- Submit button -->
            <div class="form-row form-row-full">
                <button type="submit" class="btn btn-primary btn-block">
                    Create Account
                </button>
            </div>
        </form>

        <!-- Link to login page -->
        <p class="form-foot muted">
            Already a member?
            <a href="${pageContext.request.contextPath}/login">Sign in instead</a>
        </p>
    </div>
</main>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>