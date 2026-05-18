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

<!-- Hero section with welcome message -->
<section class="hero">
    <div class="hero-inner">
        <h1>Glow with confidence,<br/>every single day.</h1>
        <p class="lead">
            Clean-formula skincare and beauty essentials,
            curated for every skin type and tone.
        </p>
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

<!-- Featured products section -->
<section class="section">
    <div class="section-head">
        <h2>Featured Picks</h2>
        <a class="muted-link" href="${pageContext.request.contextPath}/products">
            View all &rarr;
        </a>
    </div>

    <div class="product-grid">
        <!-- Loop through all featured products and display them -->
        <c:forEach var="product" items="${featuredProducts}">
            <article class="product-card">

                <!-- Product image -->
                <div class="thumb">
                    <img src="${pageContext.request.contextPath}/${product.photoPath}"
                         alt="${product.name}"
                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
                </div>

                <!-- Product name -->
                <h3>${product.name}</h3>

                <!-- Product brand -->
                <div class="brand">${product.brandName}</div>

                <!-- Product category -->
                <div class="category">${product.category}</div>

                <!-- Product price in NPR -->
                <div class="price">NPR ${product.price}</div>

                <!-- Product stock status -->
                <div class="stock ${product.inStock ? 'in-stock' : 'out-of-stock'}">
                    ${product.inStock ? 'In stock' : 'Out of stock'}
                </div>

                <!-- Product action buttons -->
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

<!-- Value strip section -->
<section class="value-strip">
    <div class="value-item">
        <h3>Clean Formulas</h3>
        <p>Free from parabens, sulfates and artificial fragrance.</p>
    </div>
    <div class="value-item">
        <h3>Cruelty-Free</h3>
        <p>Never tested on animals - certified and proud.</p>
    </div>
    <div class="value-item">
        <h3>Made With Care</h3>
        <p>Small-batch products formulated by passionate experts.</p>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>