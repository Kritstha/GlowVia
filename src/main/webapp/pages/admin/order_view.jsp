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

        <!-- Page heading with back button -->
        <div class="admin-head">
            <h1>Order #${order.orderId}</h1>
            <a class="btn" href="${pageContext.request.contextPath}/admin/orders">
                &larr; All orders
            </a>
        </div>

        <!-- Order details card -->
        <div class="card">
            <!-- Customer name -->
            <p>Customer: <strong>${order.customerName}</strong></p>

            <!-- Order date -->
            <p>Placed: ${order.orderDate}</p>

            <!-- Order status -->
            <p>Status:
                <span class="status ${order.status.toLowerCase()}">
                    ${order.status}
                </span>
            </p>

            <!-- Order total -->
            <p>Total: <strong>NPR ${order.totalAmount}</strong></p>
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
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <!-- Product name -->
                            <td>${item.productName}</td>

                            <!-- Quantity -->
                            <td>${item.quantity}</td>

                            <!-- Unit price -->
                            <td>NPR ${item.priceAtPurchase}</td>

                            <!-- Line total -->
                            <td>NPR ${item.lineTotal}</td>
                        </tr>
                    </c:forEach>

                    <!-- Show message if no items found -->
                    <c:if test="${empty order.items}">
                        <tr>
                            <td colspan="4" class="empty-cell">
                                No items found.
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