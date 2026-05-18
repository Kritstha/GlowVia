<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Access denied - Glowvia</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<%@ include file="/includes/customer_nav.jsp" %>

<section class="section">
    <div class="page-head">
        <h1>403 - Access denied</h1>
        <p class="muted">
            You don't have permission to view this page.
            Please sign in with the right account.
        </p>
        <p style="margin-top:18px">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">
                Sign in
            </a>
            <a class="btn" href="${pageContext.request.contextPath}/home">
                Go home
            </a>
        </p>
    </div>
</section>

<%@ include file="/includes/customer_footer.jsp" %>

</body>
</html>
