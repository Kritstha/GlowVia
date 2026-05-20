<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Access Denied - Glowvia</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<!--
    This includes the customer navigation bar at the top of the page
    It shows the logo, navigation links and login or logout buttons
-->
<jsp:include page="/includes/customer_nav.jsp" />

<!--
    This is the access denied section
    It is shown when a regular customer tries to access the admin panel
    by typing the admin URL directly in the browser
-->
<section class="section" style="text-align: center; padding: 80px 24px;">

    <!--
        This shows the 403 error code in large red text
        403 means the user does not have permission to view this page
    -->
    <h1 style="font-size: 5rem; color: var(--bad);">403</h1>

    <!-- This shows the access denied heading -->
    <h2>Access Denied</h2>

    <!--
        This shows a message explaining why the customer cannot view this page
        Only admin users are allowed to access the admin panel
    -->
    <p class="muted" style="margin: 16px 0;">
        Oops! You don't have permission to view this page.
        This page is only accessible to admins.
    </p>

    <!--
        This shows a button to take the customer back to the home page
        Clicking it redirects the customer to the Glowvia home page
    -->
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">
        Go to Homepage
    </a>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>