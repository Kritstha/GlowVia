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

<!--
    This is the hero section at the top of the home page
    It shows a welcome message and two buttons to shop or read about Glowvia
-->
<section class="hero">
    <div class="hero-inner">
        <h1>Glow with confidence,<br/>every single day.</h1>
        <p class="lead">
            Clean-formula skincare and beauty essentials,
            curated for every skin type and tone.
        </p>

        <!--
            This shows two call to action buttons
            Shop the Collection takes the user to the products page
            Our Story takes the user to the about page
        -->
        <div class="hero-cta">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/products">
                Shop the Collection
            </a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/about">
                Our Story
            </a>
        </div>
    </div>
</section>

<!--
    This is the featured products section on the home page
    It shows a selection of featured products from the database
-->
<section class="section">

    <!--
        This is the section heading with a link to view all products
    -->
    <div class="section-head">
        <h2>Featured Picks</h2>
        <a class="muted-link" href="${pageContext.request.contextPath}/products">
            View all &rarr;
        </a>
    </div>

    <div class="product-grid">

        <!--
            This loops through all featured products from the database
            and displays each product as a card in the grid
        -->
        <c:forEach var="product" items="${featuredProducts}">
            <article class="product-card">

                <!--
                    This shows the product image
                    If no image is found it shows a placeholder image
                -->
                <div class="thumb">
                    <img src="${pageContext.request.contextPath}/${product.photoPath}"
                         alt="${product.name}"
                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
                </div>

                <!-- This shows the name of the product -->
                <h3>${product.name}</h3>

                <!-- This shows the brand of the product -->
                <div class="brand">${product.brandName}</div>

                <!-- This shows the category of the product -->
                <div class="category">${product.category}</div>

                <!-- This shows the price of the product in NPR -->
                <div class="price">NPR ${product.price}</div>

                <!--
                    This shows the stock status of the product
                    It shows In stock in green or Out of stock in red
                -->
                <div class="stock ${product.inStock ? 'in-stock' : 'out-of-stock'}">
                    ${product.inStock ? 'In stock' : 'Out of stock'}
                </div>

                <!--
                    This shows the View button for the product
                    Clicking it takes the user to the product detail page
                -->
                <div class="card-actions">
                    <a class="btn btn-outline"
                       href="${pageContext.request.contextPath}/product?id=${product.id}">
                        View
                    </a>
                </div>
            </article>
        </c:forEach>
    </div>
</section>

<!--
    This is the value strip section at the bottom of the home page
    It shows three key values of Glowvia which are clean formulas,
    cruelty free and made with care
-->
<section class="value-strip">

    <!--
        This shows the clean formulas value
        Glowvia products are free from parabens sulfates and artificial fragrance
    -->
    <div class="value-item">
        <h3>Clean Formulas</h3>
        <p>Free from parabens, sulfates and artificial fragrance.</p>
    </div>

    <!--
        This shows the cruelty free value
        Glowvia never tests on animals and is certified cruelty free
    -->
    <div class="value-item">
        <h3>Cruelty-Free</h3>
        <p>Never tested on animals - certified and proud.</p>
    </div>

    <!--
        This shows the made with care value
        Glowvia products are small batch and formulated by passionate experts
    -->
    <div class="value-item">
        <h3>Made With Care</h3>
        <p>Small-batch products formulated by passionate experts.</p>
    </div>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>