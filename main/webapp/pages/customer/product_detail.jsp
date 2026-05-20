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


    <a class="back-link" href="${pageContext.request.contextPath}/products">
        &larr; Back to shop
    </a>

 
    <div class="detail-grid">


        <div class="detail-image">
            <img src="${pageContext.request.contextPath}/${product.photoPath}"
                 alt="${product.name}"
                 onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
        </div>

        <div class="detail-info">


            <div class="muted detail-brand">
                <c:choose>
                    <c:when test="${empty product.brandName}">Glowvia</c:when>
                    <c:otherwise>${product.brandName}</c:otherwise>
                </c:choose>
            </div>


            <h1>${product.name}</h1>


            <div class="detail-price">NPR ${product.price}</div>

         
            <div class="badge-row">
                <span class="badge">${product.category}</span>
                <span class="badge">${product.skinType}</span>
                <c:choose>
                    <c:when test="${product.inStock}">
                        <span class="badge ok">In stock</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bad">Out of stock</span>
                    </c:otherwise>
                </c:choose>
            </div>


            <p class="detail-desc">${product.description}</p>


            <div class="detail-fact">
                <strong>Key ingredients:</strong>
                <span>
                    <c:choose>
                        <c:when test="${empty product.keyIngredients}">-</c:when>
                        <c:otherwise>${product.keyIngredients}</c:otherwise>
                    </c:choose>
                </span>
            </div>

            <!--
                This shows the add to cart form
                It is only shown when the user is logged in and the product is in stock
            -->
            <c:if test="${not empty sessionScope.currentUser && product.inStock}">
                <form method="post" action="${pageContext.request.contextPath}/cart/add"
                      class="detail-form">
                    <input type="hidden" name="productId" value="${product.id}"/>

                    <!--
                        This is the quantity input field
                        Customer can select how many units they want to add to cart
                    -->
                    <label for="quantity">Quantity:</label>
                    <input id="quantity" name="quantity" type="number" min="1"
                           max="${product.stockQuantity}" value="1"/>
                    <button type="submit" class="btn btn-primary">Add to Cart</button>
                </form>
            </c:if>

            <!--
                This shows a sign in button if the user is not logged in
                Customer must sign in before they can add products to cart
            -->
            <c:if test="${empty sessionScope.currentUser}">
                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/login">
                    Sign in to add to cart
                </a>
            </c:if>
        </div>
    </div>

 
    <div class="reviews-section" style="margin-top: 40px;">
        <h2>Customer Reviews</h2>

        <!--
            This checks if there is a success message in the session
            If yes it shows a green toast message and removes it from session
        -->
        <c:if test="${not empty sessionScope.success}">
            <div class="toast toast-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session"/>
        </c:if>

        <!--
            This checks if there is an error message in the session
            If yes it shows a red toast message and removes it from session
        -->
        <c:if test="${not empty sessionScope.error}">
            <div class="toast toast-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>


        <c:if test="${not empty sessionScope.currentUser && !hasReviewed}">
            <div class="card" style="margin-bottom: 20px;">
                <h3>Write a Review</h3>
                <form method="post" action="${pageContext.request.contextPath}/review/add">
                    <input type="hidden" name="productId" value="${product.id}"/>


                    <div class="form-row">
                        <label for="rating">Rating</label>
                        <select id="rating" name="rating" required>
                            <option value="5">⭐⭐⭐⭐⭐ - Excellent</option>
                            <option value="4">⭐⭐⭐⭐ - Good</option>
                            <option value="3">⭐⭐⭐ - Average</option>
                            <option value="2">⭐⭐ - Poor</option>
                            <option value="1">⭐ - Terrible</option>
                        </select>
                    </div>

         
                    <div class="form-row">
                        <label for="comment">Comment</label>
                        <textarea id="comment" name="comment" rows="3"
                                  placeholder="Share your experience..."></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary">Submit Review</button>
                </form>
            </div>
        </c:if>

        <!--
            This shows a message if the logged in user has already reviewed this product
            Each customer can only leave one review per product
        -->
        <c:if test="${not empty sessionScope.currentUser && hasReviewed}">
            <div class="toast toast-success" style="margin-bottom: 20px;">
                You have already reviewed this product!
            </div>
        </c:if>

        <!--
            This loops through all reviews for this product
            and displays each review as a card with name, rating, comment and date
        -->
        <c:forEach var="review" items="${reviews}">
            <div class="card" style="margin-bottom: 15px;">

                <!--
                    This shows the reviewer name on the left
                    and the star rating on the right
                -->
                <div style="display: flex; justify-content: space-between;">
                    <strong>${review.get('fullName')}</strong>
                    <span>
                        <c:forEach begin="1" end="${review.get('rating')}" var="star">⭐</c:forEach>
                    </span>
                </div>

                <!-- This shows the review comment written by the customer -->
                <p style="margin-top: 8px;">${review.get('comment')}</p>

                <!-- This shows the date when the review was submitted -->
                <small class="muted">${review.get('createdAt')}</small>
            </div>
        </c:forEach>

   
        <c:if test="${empty reviews}">
            <div class="empty-msg">
                No reviews yet. Be the first to review this product!
            </div>
        </c:if>
    </div>
</section>


<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>