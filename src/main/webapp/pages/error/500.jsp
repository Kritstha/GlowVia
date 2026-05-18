<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Something went wrong - Glowvia</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<%@ include file="/includes/customer_nav.jsp" %>

<section class="section">
    <div class="page-head">
        <h1>500 - Server error</h1>
        <p class="muted">
            Something went wrong on our end.
            Our team has been notified - please try again in a few moments.
        </p>
        <p style="margin-top:18px">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">
                Back to home
            </a>
        </p>
    </div>
</section>

<%@ include file="/includes/customer_footer.jsp" %>

</body>
</html>
