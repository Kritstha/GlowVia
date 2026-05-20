<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css"/>
</head>
<body class="admin-body">

<div class="admin-shell">
    <jsp:include page="/includes/admin_sidebar.jsp" />

    <main class="admin-main">
        <jsp:include page="/includes/flash.jsp" />

        <!-- Page heading -->
        <div class="admin-head">
            <h1>Reviews</h1>
            <p class="muted">Manage customer product reviews.</p>
        </div>

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

        <!-- Reviews table -->
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Customer</th>
                        <th>Rating</th>
                        <th>Comment</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Loop through all reviews and display them -->
                    <c:forEach var="review" items="${reviews}">
                        <tr>
                            <!-- Product name -->
                            <td>${review.get('productName')}</td>

                            <!-- Customer name -->
                            <td>${review.get('fullName')}</td>

                            <!-- Rating stars -->
                            <td>
                                <c:forEach begin="1" end="${review.get('rating')}" var="star">⭐</c:forEach>
                            </td>

                            <!-- Review comment -->
                            <td>${review.get('comment')}</td>

                            <!-- Review date -->
                            <td>${review.get('createdAt')}</td>

                            <!-- Delete button -->
                            <td class="action-cell">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/review/delete"
                                      onsubmit="return confirm('Delete this review?');"
                                      class="inline-form">
                                    <input type="hidden" name="id" value="${review.get('id')}"/>
                                    <button class="btn btn-small btn-danger" type="submit">
                                        Delete
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Show message if no reviews found -->
                    <c:if test="${empty reviews}">
                        <tr>
                            <td colspan="6" class="empty-cell">
                                No reviews yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</div>

</body>
</html>
