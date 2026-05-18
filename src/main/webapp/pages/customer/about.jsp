<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="customer-body">

<%@ include file="/includes/customer_nav.jsp" %>
<%@ include file="/includes/flash.jsp" %>

<section class="section">
    <div class="page-head">
        <h1>About Glowvia</h1>
        <p class="muted">Beauty that feels good - inside and out.</p>
    </div>

    <div class="prose">
        <p>
            Glowvia began as a passion project: a small team determined to prove that
            high-performance skincare and beauty could be created without compromise.
            Today we ship clean, cruelty-free products that work with your skin,
            not against it.
        </p>

        <h2>Our promise</h2>
        <ul>
            <li>Every formula is reviewed for safety, efficacy and sustainability.</li>
            <li>We never test on animals and partner only with cruelty-free suppliers.</li>
            <li>Honest labels - if it's in the bottle, it's on the box.</li>
        </ul>

        <h2>Our community</h2>
        <p>
            Glowvia is for every skin tone, type and story. We believe great skincare
            should be inclusive, affordable and delightfully simple to use.
        </p>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>
