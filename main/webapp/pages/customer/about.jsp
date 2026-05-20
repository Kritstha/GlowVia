<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            <li>Honest labels - if it is in the bottle, it is on the box.</li>
        </ul>

        <h2>Our community</h2>
        <p>
            Glowvia is for every skin tone, type and story. We believe great skincare
            should be inclusive, affordable and delightfully simple to use.
        </p>
    </div>


    <div class="team-section">
        <h2>Meet the Team</h2>
        <p class="muted">The passionate people behind Glowvia.</p>

        <div class="team-grid">

  
            <div class="team-member-card">
                <img src="${pageContext.request.contextPath}/images/team/kritagya.jpg"
                     alt="Kritagya Shrestha" class="member-photo"/>
                <h3>Kritagya Shrestha</h3>
                <p class="role">Project Leader & Product Manager</p>
                <p class="bio-text">Kritagya leads the Glowvia project, managing the team and ensuring the project looking over fullstack development focusing on a proper website.</p>
                <p class="member-detail"><strong>Education:</strong> BSc(Hons) Computing</p>
                <p class="member-detail"><strong>College:</strong> Islington College, Kathmandu</p>
            </div>

            <div class="team-member-card">
                <img src="${pageContext.request.contextPath}/images/team/krishna.jpg"
                     alt="Ramkrishna Poudel" class="member-photo"/>
                <h3>Ramkrishna Poudel</h3>
                <p class="role">Full-Stack Developer</p>
                <p class="bio-text">Ramkrishna works on both frontend and backend of the Glowvia platform bringing creativity and technical expertise as well as APIs .</p>
                <p class="member-detail"><strong>Education:</strong> BSc(Hons) Computing</p>
                <p class="member-detail"><strong>College:</strong> Islington College, Kathmandu</p>
            </div>

 
            <div class="team-member-card">
                <img src="${pageContext.request.contextPath}/images/team/Shubham.png"
                     alt="Shubham Tiwari" class="member-photo"/>
                <h3>Shubham Tiwari</h3>
                <p class="role">Partial-Backend Developer</p>
                <p class="bio-text">Shubham builds the server side logic  of Glowvia ensuring everything runs smoothly behind the scenes also cart logic and database structure.</p>
                <p class="member-detail"><strong>Education:</strong> BSc(Hons) Computing</p>
                <p class="member-detail"><strong>College:</strong> Islington College, Kathmandu</p>
            </div>



            <div class="team-member-card">
                <img src="${pageContext.request.contextPath}/images/team/vishal.jpg"
                     alt="Vishal Singh" class="member-photo"/>
                <h3>Vishal Singh</h3>
                <p class="role">Design Developer</p>
                <p class="bio-text">Vishal is responsible for database conectivity and dashboard UI supporting both frontend and backend .</p>
                <p class="member-detail"><strong>Education:</strong> BSc(Hons) Computing</p>
                <p class="member-detail"><strong>College:</strong> Islington College, Kathmandu</p>
            </div>


            <div class="team-member-card">
                <img src="${pageContext.request.contextPath}/images/team/nidesh.jpg"
                     alt="Nidesh Byanjankar" class="member-photo"/>
                <h3>Nidesh Byanjankar</h3>
                <p class="role">Partial-Frontend Developer</p>
                <p class="bio-text">Nidesh designs and manages the Glowvia review and contact us controller, pages UI/UX design, UI improvement.</p>
                <p class="member-detail"><strong>Education:</strong> BSc(Hons) Computing</p>
                <p class="member-detail"><strong>College:</strong> Islington College, Kathmandu</p>
            </div>


            <div class="team-member-card">
                <img src="${pageContext.request.contextPath}/images/team/salina.jpg"
                     alt="Salina Karki" class="member-photo"/>
                <h3>Salina Karki</h3>
                <p class="role">Security Specialist</p>
                <p class="bio-text">Salina created security & authorization Admin Controllers,Login/Admin Filters</p>
                <p class="member-detail"><strong>Education:</strong> BSc(Hons) Computing</p>
                <p class="member-detail"><strong>College:</strong> Islington College, Kathmandu</p>
            </div>

        </div>
    </div>
</section>


<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>