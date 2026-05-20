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
        This is the back link that takes the customer back to all orders page
    -->
    <a class="back-link" href="${pageContext.request.contextPath}/orders">
        &larr; All orders
    </a>

    <!--
        This is the order heading section
        It shows the order number, date it was placed and current status
    -->
    <div class="page-head">
        <h1>Order #${order.orderId}</h1>
        <p class="muted">
            Placed ${order.orderDate} -
            Status:
            <!--
                This shows the order status with a colored badge
                Status can be Pending, Confirmed, Shipped, Delivered or Cancelled
            -->
            <span class="status ${order.status.toLowerCase()}">
                ${order.status}
            </span>
        </p>
    </div>

    <!--
        This table shows all the items that were ordered
        Each row shows the product name, quantity, unit price and line total
    -->
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

                <!--
                    This loops through all order items
                    and displays each item in a table row
                -->
                <c:forEach var="item" items="${orderItems}">
                    <tr>

                        <!-- This shows the name of the product that was ordered -->
                        <td>${item.get('productName')}</td>

                        <!-- This shows how many units of the product were ordered -->
                        <td>${item.get('quantity')}</td>

                        <!-- This shows the price per unit at the time of purchase -->
                        <td>NPR ${item.get('priceAtPurchase')}</td>

                        <!-- This shows the total price for this item -->
                        <td>NPR ${item.get('lineTotal')}</td>
                    </tr>
                </c:forEach>

                <!--
                    This shows a message if there are no items found for this order
                -->
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

    <!--
        This section shows the total amount of the order
        It displays the sum of all item line totals in NPR
    -->
    <div class="cart-summary">
        <div class="cart-total">
            <span>Order total</span>
            <strong>NPR ${order.totalAmount}</strong>
        </div>
    </div>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>