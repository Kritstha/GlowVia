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
        It shows the title and a short description of the contact page
    -->
    <div class="page-head">
        <h1>Contact Us</h1>
        <p class="muted">We'd love to hear from you.</p>
    </div>

    <!--
        This section shows two cards side by side
        One card shows support hours and contact details
        The other card shows the contact form for sending a message
    -->
    <div class="two-col">

        <!--
            This card shows the support hours and contact information
            It includes email, phone number and office address
        -->
        <div class="card">
            <h2>Support hours</h2>
            <p class="muted">Monday - Friday, 9am - 5pm (local time)</p>
            <p>Email: support@glowvia.com</p>
            <p>Phone: +977 9843234568</p>
            <p>Office: Putalisadak, Nepal</p>
        </div>

        <!--
            This card shows the contact form
            Customer can enter their name, email and message to send to Glowvia
        -->
        <div class="card">
            <h2>Send us a note</h2>
            <form method="post" action="${pageContext.request.contextPath}/contact"
                  class="form">

                <!--
                    This is the name input field
                    Customer must enter their full name to submit the form
                -->
                <div class="form-row">
                    <label for="name">Your name</label>
                    <input id="name" name="name" type="text" required/>
                </div>

                <!--
                    This is the email input field
                    Customer must enter a valid email address to submit the form
                -->
                <div class="form-row">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" required/>
                </div>

                <!--
                    This is the message textarea
                    Customer types their message or question here
                -->
                <div class="form-row">
                    <label for="message">Message</label>
                    <textarea id="message" name="message" rows="5" required></textarea>
                </div>

                <!--
                    This is the submit button
                    Customer clicks this to send the message to Glowvia
                -->
                <button type="submit" class="btn btn-primary">Send Message</button>
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