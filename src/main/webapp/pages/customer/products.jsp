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

    <!-- Page heading -->
    <div class="page-head">
        <h1>Our Collection</h1>
        <p class="muted">Clean, conscious beauty - crafted for every skin type.</p>
    </div>

    <!-- Search form -->
    <form method="get" action="${pageContext.request.contextPath}/products" class="search-form">
        <input type="text" name="q" placeholder="Search by name, ingredient, brand..."
               value="${searchKeyword}"/>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <!-- Show total number of products found -->
    <div class="result-count muted">
        ${products.size()} products found
    </div>

    <!-- Product grid - loop through all products and display them -->
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
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

        <!-- Show message if no products found -->
        <c:if test="${empty products}">
            <div class="empty-msg">
                No products found. Please try a different search.
            </div>
        </c:if>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>