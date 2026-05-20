<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Something went wrong - Glowvia</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<!--
    This includes the customer navigation bar at the top of the page
    It shows the logo, navigation links and login or logout buttons
-->
<jsp:include page="/includes/customer_nav.jsp" />

<!--
    This is the 500 server error section
    It is shown when something goes wrong on the server side
    For example when the database is down or there is an unexpected error in the code
-->
<section class="section">
    <div class="page-head">

        <!--
            This shows the 500 error code and server error message
            500 means something went wrong on the server and it could not process the request
        -->
        <h1>500 - Server error</h1>
        <p class="muted">
            Something went wrong on our end.
            Our team has been notified - please try again in a few moments.
        </p>

        <!--
            This shows a button to take the customer back to the home page
            Clicking it redirects the customer to the Glowvia home page
        -->
        <p style="margin-top:18px">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">
                Back to home
            </a>
        </p>
    </div>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>