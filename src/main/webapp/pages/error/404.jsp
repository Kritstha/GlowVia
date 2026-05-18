<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Page not found - Glowvia</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<jsp:include page="/includes/customer_nav.jsp" />

<section class="section">
    <div class="page-head">
        <h1>404 - Page not found</h1>
        <p class="muted">
            We couldn't find what you were looking for.
            It may have been moved or deleted.
        </p>
        <p style="margin-top:18px">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">
                Back to home
            </a>
        </p>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>
