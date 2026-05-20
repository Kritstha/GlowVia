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


    <a class="back-link" href="${pageContext.request.contextPath}/orders">
        &larr; All orders
    </a>

 
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

    
                        <td>${item.get('productName')}</td>

                   
                        <td>${item.get('quantity')}</td>

              
                        <td>NPR ${item.get('priceAtPurchase')}</td>

              
                        <td>NPR ${item.get('lineTotal')}</td>
                    </tr>
                </c:forEach>

         
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


<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>