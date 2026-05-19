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

<jsp:include page="/includes/customer_nav.jsp" />

<section class="section" style="text-align: center; padding: 80px 24px;">
    <!-- Access denied icon -->
    <h1 style="font-size: 5rem; color: var(--bad);">403</h1>
    <h2>Access Denied</h2>
    <p class="muted" style="margin: 16px 0;">
        Oops! You don't have permission to view this page.
        This page is only accessible to admins.
    </p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">
        Go to Homepage
    </a>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>