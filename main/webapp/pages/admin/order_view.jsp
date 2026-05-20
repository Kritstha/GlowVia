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

<!-- This includes the admin sidebar navigation 
It shows all the admin menu links on the left side-->

    <jsp:include page="/includes/admin_sidebar.jsp" />

    <main class="admin-main">
        <jsp:include page="/includes/flash.jsp" />

        <!-- This is the page heading section
            It shows the title and description of the brands page -->
        <div class="admin-head">
            <h1>Order #${order.orderId}</h1>
            <a class="btn" href="${pageContext.request.contextPath}/admin/orders">
                &larr; All orders
            </a>
        </div>

        <!-- This checks if there is a success message in the session
            If yes it shows a green toast message and removes it from session.-->
        
        <div class="card">
            <!-- This code shows the customer name in admin page -->
            <p>Customer: <strong>${order.customerName}</strong></p>

            <!-- this shows the order date of the product when customer buy it -->
            <p>Placed: ${order.orderDate}</p>

            <!-- it shows whether order is pending or sucessful  -->
            <p>Status:
                <span class="status ${order.status.toLowerCase()}">
                    ${order.status}
                </span>
            </p>

            <!-- It shows total order amount  -->
            <p>Total: <strong>NPR ${order.totalAmount}</strong></p>
        </div>

        <!-- it shows order items table -->
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
                    <!-- it Loop through all order items and display them -->
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

                    <!-- this block of code show message if  items didn't found -->
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