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
<body class="customer-body">

<jsp:include page="/includes/customer_nav.jsp" />
<jsp:include page="/includes/flash.jsp" />

<section class="section">

    <!-- Page heading -->
    <div class="page-head">
        <h1>My Profile</h1>
        <p class="muted">Update your account information.</p>
    </div>

    <!-- Show success message if profile updated -->
    <c:if test="${not empty sessionScope.success}">
        <div class="toast toast-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <!-- Show error message if something went wrong -->
    <c:if test="${not empty sessionScope.error}">
        <div class="toast toast-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="two-col">

        <!-- Account details form -->
        <div class="card">
            <h2>Account details</h2>
            <form method="post" action="${pageContext.request.contextPath}/profile"
                  class="form">

                <!-- Username field disabled cannot be changed -->
                <div class="form-row">
                    <label>Username (cannot be changed)</label>
                    <input type="text" value="${user.username}" disabled/>
                </div>

                <!-- Full name field -->
                <div class="form-row">
                    <label for="fullName">Full Name</label>
                    <input id="fullName" name="fullName" type="text" required
                           value="${user.fullName}"/>
                </div>

                <!-- Email field -->
                <div class="form-row">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" required
                           value="${user.email}"/>
                </div>

                <!-- Phone field -->
                <div class="form-row">
                    <label for="phone">Phone</label>
                    <input id="phone" name="phone" type="tel" required
                           value="${user.phone}"/>
                </div>

                <!-- Date of birth field -->
                <div class="form-row">
                    <label for="dob">Date of Birth</label>
                    <input id="dob" name="dob" type="date" value="${user.dob}"/>
                </div>

                <!-- Gender field -->
                <div class="form-row">
                    <label for="gender">Gender</label>
                    <input id="gender" name="gender" type="text"
                           value="${user.gender}"/>
                </div>

                <button type="submit" class="btn btn-primary">Save changes</button>
            </form>
        </div>

        <!-- Change password form -->
        <div class="card">
            <h2>Change password</h2>
            <form method="post" action="${pageContext.request.contextPath}/profile/password"
                  class="form">

                <!-- Current password field -->
                <div class="form-row">
                    <label for="currentPassword">Current password</label>
                    <input id="currentPassword" name="currentPassword"
                           type="password" required/>
                </div>

                <!-- New password field -->
                <div class="form-row">
                    <label for="newPassword">New password</label>
                    <input id="newPassword" name="newPassword"
                           type="password" required minlength="6"/>
                </div>

                <!-- Confirm new password field -->
                <div class="form-row">
                    <label for="confirmPassword">Confirm new password</label>
                    <input id="confirmPassword" name="confirmPassword"
                           type="password" required minlength="6"/>
                </div>

                <button type="submit" class="btn btn-primary">Update password</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>