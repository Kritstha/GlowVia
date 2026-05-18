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
            <h1>Orders</h1>
            <p class="muted">Manage incoming orders and shipment status.</p>
        </div>

        <!-- Orders table -->
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Order #</th>
                        <th>Customer</th>
                        <th>Placed</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th>Update</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Loop through all orders and display them -->
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <!-- Order id -->
                            <td>#${order.orderId}</td>

                            <!-- Customer name -->
                            <td>${order.customerName}</td>

                            <!-- Order date -->
                            <td>${order.orderDate}</td>

                            <!-- Order total -->
                            <td>NPR ${order.totalAmount}</td>

                            <!-- Order status -->
                            <td>
                                <span class="status ${order.status.toLowerCase()}">
                                    ${order.status}
                                </span>
                            </td>

                            <!-- Update order status form -->
                            <td>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/orders"
                                      class="inline-form">
                                    <input type="hidden" name="orderId" value="${order.orderId}"/>
                                    <select name="status" class="qty-input">
                                        <option value="Pending" ${order.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                        <option value="Confirmed" ${order.status == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                        <option value="Shipped" ${order.status == 'Shipped' ? 'selected' : ''}>Shipped</option>
                                        <option value="Delivered" ${order.status == 'Delivered' ? 'selected' : ''}>Delivered</option>
                                        <option value="Cancelled" ${order.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                    <button class="btn btn-small" type="submit">Save</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Show message if no orders found -->
                    <c:if test="${empty orders}">
                        <tr>
                            <td colspan="6" class="empty-cell">
                                No orders yet.
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