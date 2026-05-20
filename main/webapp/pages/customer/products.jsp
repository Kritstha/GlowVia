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
        <h1>Our Collection</h1>
        <p class="muted">Clean, conscious beauty - crafted for every skin type.</p>
    </div>

    <!--
        This is the search form
        Customer can search for products by name, ingredient or brand
    -->
    <form method="get" action="${pageContext.request.contextPath}/products" class="search-form">
        <input type="text" name="q" placeholder="Search by name, ingredient, brand..."
               value="${searchKeyword}"/>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>


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


                <div class="thumb">
                    <img src="${pageContext.request.contextPath}/${product.photoPath}"
                         alt="${product.name}"
                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
                </div>

   
                <h3>${product.name}</h3>


                <div class="brand">${product.brandName}</div>

   
                <div class="category">${product.category}</div>


                <div class="price">NPR ${product.price}</div>


                <div class="stock ${product.inStock ? 'in-stock' : 'out-of-stock'}">
                    ${product.inStock ? 'In stock' : 'Out of stock'}
                </div>

   
                <div class="card-actions">
                    <a class="btn btn-outline"
                       href="${pageContext.request.contextPath}/product?id=${product.id}">
                        View
                    </a>
                </div>
            </article>
        </c:forEach>

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