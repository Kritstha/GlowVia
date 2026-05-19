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

<jsp:include page="/includes/customer_nav.jsp" />
<jsp:include page="/includes/flash.jsp" />

<section class="section">
    <div class="page-head">
        <h1>Contact Us</h1>
        <p class="muted">We'd love to hear from you.</p>
    </div>

    <div class="two-col">
        <div class="card">
            <h2>Support hours</h2>
            <p class="muted">Monday - Friday, 9am - 5pm (local time)</p>
            <p>Email: support@glowvia.com</p>
            <p>Phone: +977 9843234568</p>
            <p>Office: Putalisadak, Nepal</p>
        </div>

        <div class="card">
            <h2>Send us a note</h2>
            <form method="post" action="${pageContext.request.contextPath}/contact"
                  class="form">
                <div class="form-row">
                    <label for="name">Your name</label>
                    <input id="name" name="name" type="text" required/>
                </div>
                <div class="form-row">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" required/>
                </div>
                <div class="form-row">
                    <label for="message">Message</label>
                    <textarea id="message" name="message" rows="5" required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Send Message</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>
