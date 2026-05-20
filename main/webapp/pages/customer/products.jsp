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

<section class="section">

    <!--
        This is the page heading section
        It shows the title and a short description of the products page
    -->
    <div class="page-head">
        <h1>Our Collection</h1>
        <p class="muted">Clean, conscious beauty - crafted for every skin type.</p>
    </div>

    <!--
        This is the search form
        Customer can search for products by name, ingredient or brand
        The form submits to the products page using GET method
    -->
    <form method="get" action="${pageContext.request.contextPath}/products" class="search-form">
        <input type="text" name="q" placeholder="Search by name, ingredient, brand..."
               value="${searchKeyword}"/>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <!--
        This shows the total number of products found
        If a search is done it shows how many results matched the search
    -->
    <div class="result-count muted">
        ${products.size()} products found
    </div>

    <!--
        This is the product grid section
        It loops through all products and displays each one as a card
    -->
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
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
                    Clicking it takes the customer to the product detail page
                -->
                <div class="card-actions">
                    <a class="btn btn-outline"
                       href="${pageContext.request.contextPath}/product?id=${product.id}">
                        View
                    </a>
                </div>
            </article>
        </c:forEach>

        <!--
            This shows a message if no products are found
            This happens when a search returns no results
        -->
        <c:if test="${empty products}">
            <div class="empty-msg">
                No products found. Please try a different search.
            </div>
        </c:if>
    </div>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>