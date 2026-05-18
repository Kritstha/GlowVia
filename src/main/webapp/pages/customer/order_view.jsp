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

    <!-- Back link to all orders -->
    <a class="back-link" href="${pageContext.request.contextPath}/orders">
        &larr; All orders
    </a>

    <!-- Order heading -->
    <div class="page-head">
        <h1>Order #${order.orderId}</h1>
        <p class="muted">
            Placed ${order.orderDate} -
            Status:
            <span class="status ${order.status.toLowerCase()}">
                ${order.status}
            </span>
        </p>
    </div>

    <!-- Order items table -->
    <div class="table-wrap">
        <table class="data-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>Unit Price</th>
                    <th>Line Total</th>
                </tr>
            </thead>
            <tbody>
                <!-- Loop through all order items and display them -->
                <c:forEach var="item" items="${orderItems}">
                    <tr>
                        <!-- Product name -->
                        <td>${item.get('productName')}</td>

                        <!-- Quantity -->
                        <td>${item.get('quantity')}</td>

                        <!-- Unit price -->
                        <td>NPR ${item.get('priceAtPurchase')}</td>

                        <!-- Line total -->
                        <td>NPR ${item.get('lineTotal')}</td>
                    </tr>
                </c:forEach>

                <!-- Show message if no items found -->
                <c:if test="${empty orderItems}">
                    <tr>
                        <td colspan="4" class="empty-cell">
                            No items found.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <!-- Order total -->
    <div class="cart-summary">
        <div class="cart-total">
            <span>Order total</span>
            <strong>NPR ${order.totalAmount}</strong>
        </div>
    </div>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>