<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Page not found - Glowvia</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<!--
    This includes the customer navigation bar at the top of the page
    It shows the logo, navigation links and login or logout buttons
-->
<jsp:include page="/includes/customer_nav.jsp" />

<!--
    This is the 404 error section
    It is shown when the customer tries to visit a page that does not exist
    For example when they type a wrong URL or the page has been moved or deleted
-->
<section class="section">
    <div class="page-head">

        <!--
            This shows the 404 error code and page not found message
            404 means the page the customer is looking for could not be found
        -->
        <h1>404 - Page not found</h1>
        <p class="muted">
            We couldn't find what you were looking for.
            It may have been moved or deleted.
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