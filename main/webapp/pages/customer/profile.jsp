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

<section class="section">

    <!--
        This is the page heading section
        It shows the title and a short description of the profile page
    -->
    <div class="page-head">
        <h1>My Profile</h1>
        <p class="muted">Update your account information.</p>
    </div>

    <!--
        This checks if there is a success message in the session
        If yes it shows a green toast message and removes it from session
    -->
    <c:if test="${not empty sessionScope.success}">
        <div class="toast toast-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <!--
        This checks if there is an error message in the session
        If yes it shows a red toast message and removes it from session
    -->
    <c:if test="${not empty sessionScope.error}">
        <div class="toast toast-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!--
        This section shows two cards side by side
        One card shows the account details form
        The other card shows the change password form
    -->
    <div class="two-col">

        <!--
            This card shows the account details form
            Customer can update their full name, email, phone, date of birth and gender
        -->
        <div class="card">
            <h2>Account details</h2>
            <form method="post" action="${pageContext.request.contextPath}/profile"
                  class="form">

                <!--
                    This shows the username field which is disabled
                    Username cannot be changed after registration
                -->
                <div class="form-row">
                    <label>Username (cannot be changed)</label>
                    <input type="text" value="${user.username}" disabled/>
                </div>

                <!--
                    This is the full name input field
                    Customer can update their full name here
                -->
                <div class="form-row">
                    <label for="fullName">Full Name</label>
                    <input id="fullName" name="fullName" type="text" required
                           value="${user.fullName}"/>
                </div>

                <!--
                    This is the email input field
                    Customer can update their email address here
                -->
                <div class="form-row">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" required
                           value="${user.email}"/>
                </div>

                <!--
                    This is the phone input field
                    Customer can update their phone number here
                -->
                <div class="form-row">
                    <label for="phone">Phone</label>
                    <input id="phone" name="phone" type="tel" required
                           value="${user.phone}"/>
                </div>

                <!--
                    This is the date of birth input field
                    Customer can update their date of birth here
                -->
                <div class="form-row">
                    <label for="dob">Date of Birth</label>
                    <input id="dob" name="dob" type="date" value="${user.dob}"/>
                </div>

                <!--
                    This is the gender input field
                    Customer can update their gender here
                -->
                <div class="form-row">
                    <label for="gender">Gender</label>
                    <input id="gender" name="gender" type="text"
                           value="${user.gender}"/>
                </div>

                <!--
                    This is the save button
                    Customer clicks this to save the updated account details
                -->
                <button type="submit" class="btn btn-primary">Save changes</button>
            </form>
        </div>

        <!--
            This card shows the change password form
            Customer can update their password by entering current and new password
        -->
        <div class="card">
            <h2>Change password</h2>
            <form method="post" action="${pageContext.request.contextPath}/profile/password"
                  class="form">

                <!--
                    This is the current password input field
                    Customer must enter their current password to verify their identity
                -->
                <div class="form-row">
                    <label for="currentPassword">Current password</label>
                    <input id="currentPassword" name="currentPassword"
                           type="password" required/>
                </div>

                <!--
                    This is the new password input field
                    Customer enters their new password which must be at least 6 characters
                -->
                <div class="form-row">
                    <label for="newPassword">New password</label>
                    <input id="newPassword" name="newPassword"
                           type="password" required minlength="6"/>
                </div>

                <!--
                    This is the confirm new password input field
                    Customer must type the new password again to confirm it matches
                -->
                <div class="form-row">
                    <label for="confirmPassword">Confirm new password</label>
                    <input id="confirmPassword" name="confirmPassword"
                           type="password" required minlength="6"/>
                </div>

                <!--
                    This is the update password button
                    Customer clicks this to save the new password
                -->
                <button type="submit" class="btn btn-primary">Update password</button>
            </form>
        </div>
    </div>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>