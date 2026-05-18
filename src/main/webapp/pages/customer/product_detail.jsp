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

    <!-- Back link to products page -->
    <a class="back-link" href="${pageContext.request.contextPath}/products">
        &larr; Back to shop
    </a>

    <div class="detail-grid">

        <!-- Product image -->
        <div class="detail-image">
            <img src="${pageContext.request.contextPath}/${product.photoPath}"
                 alt="${product.name}"
                 onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
        </div>

        <div class="detail-info">

            <!-- Product brand -->
            <div class="muted detail-brand">
                <c:choose>
                    <c:when test="${empty product.brandName}">Glowvia</c:when>
                    <c:otherwise>${product.brandName}</c:otherwise>
                </c:choose>
            </div>

            <!-- Product name -->
            <h1>${product.name}</h1>

            <!-- Product price in NPR -->
            <div class="detail-price">NPR ${product.price}</div>

            <!-- Product badges -->
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

            <!-- Product description -->
            <p class="detail-desc">${product.description}</p>

            <!-- Product key ingredients -->
            <div class="detail-fact">
                <strong>Key ingredients:</strong>
                <span>
                    <c:choose>
                        <c:when test="${empty product.keyIngredients}">-</c:when>
                        <c:otherwise>${product.keyIngredients}</c:otherwise>
                    </c:choose>
                </span>
            </div>

            <!-- Add to cart form - only show if user is logged in and product is in stock -->
            <c:if test="${not empty sessionScope.currentUser && product.inStock}">
                <form method="post" action="${pageContext.request.contextPath}/cart/add"
                      class="detail-form">
                    <input type="hidden" name="productId" value="${product.id}"/>
                    <label for="quantity">Quantity:</label>
                    <input id="quantity" name="quantity" type="number" min="1"
                           max="${product.stockQuantity}" value="1"/>
                    <button type="submit" class="btn btn-primary">Add to Cart</button>
                </form>
            </c:if>

            <!-- Show sign in button if user is not logged in -->
            <c:if test="${empty sessionScope.currentUser}">
                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/login">
                    Sign in to add to cart
                </a>
            </c:if>
        </div>
    </div>

    <!-- Reviews section -->
    <div class="reviews-section" style="margin-top: 40px;">
        <h2>Customer Reviews</h2>

        <!-- Show success message if any -->
        <c:if test="${not empty sessionScope.success}">
            <div class="toast toast-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session"/>
        </c:if>

        <!-- Show error message if any -->
        <c:if test="${not empty sessionScope.error}">
            <div class="toast toast-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Add review form - only show if user is logged in and has not reviewed yet -->
        <c:if test="${not empty sessionScope.currentUser && !hasReviewed}">
            <div class="card" style="margin-bottom: 20px;">
                <h3>Write a Review</h3>
                <form method="post" action="${pageContext.request.contextPath}/review/add">
                    <input type="hidden" name="productId" value="${product.id}"/>

                    <!-- Rating dropdown -->
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

                    <!-- Comment field -->
                    <div class="form-row">
                        <label for="comment">Comment</label>
                        <textarea id="comment" name="comment" rows="3"
                                  placeholder="Share your experience..."></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary">Submit Review</button>
                </form>
            </div>
        </c:if>

        <!-- Show message if user already reviewed -->
        <c:if test="${not empty sessionScope.currentUser && hasReviewed}">
            <div class="toast toast-success" style="margin-bottom: 20px;">
                You have already reviewed this product!
            </div>
        </c:if>

        <!-- Show all reviews -->
        <c:forEach var="review" items="${reviews}">
            <div class="card" style="margin-bottom: 15px;">
                <!-- Reviewer name and rating -->
                <div style="display: flex; justify-content: space-between;">
                    <strong>${review.get('fullName')}</strong>
                    <span>
                        <c:forEach begin="1" end="${review.get('rating')}" var="star">⭐</c:forEach>
                    </span>
                </div>
                <!-- Review comment -->
                <p style="margin-top: 8px;">${review.get('comment')}</p>
                <!-- Review date -->
                <small class="muted">${review.get('createdAt')}</small>
            </div>
        </c:forEach>

        <!-- Show message if no reviews yet -->
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