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
<!--This includes the admin sidebar navigation
        It shows all the admin menu links on the left side-->
    <jsp:include page="/includes/admin_sidebar.jsp" />

    <main class="admin-main">
        <jsp:include page="/includes/flash.jsp" />

        <!-- This is the page heading section
            It shows the title and description of the orders page -->
        <div class="admin-head">
            <h1>Orders</h1>
            <p class="muted">Manage incoming orders and shipment status.</p>
        </div>

        <!-- This table shows all orders placed by customers
            Admin can view order details and update the order status from here -->
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
                    <!-- This loops through all orders from the database
                        and displays each order in a table row -->
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <!-- This shows the unique order id number -->
                            <td>#${order.orderId}</td>

                            <!-- This shows the name of the customer who placed the order -->
                            <td>${order.customerName}</td>

                            <!-- This shows the date when the order was placed -->
                            <td>${order.orderDate}</td>

                            <!-- This shows the total amount of the order in NPR -->
                            <td>NPR ${order.totalAmount}</td>

                            <!-- This shows the current status of the order
                                with a colored badge based on the status -->
                            <td>
                                <span class="status ${order.status.toLowerCase()}">
                                    ${order.status}
                                </span>
                            </td>

                            <!-- This is the update status form for each order
                                Admin can select a new status and click Save to update it -->
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

                    <!-- This shows a message if there are no orders in the database yet -->
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