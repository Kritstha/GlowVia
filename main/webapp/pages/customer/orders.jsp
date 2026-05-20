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
        This is the page heading section
        It shows the title and the total number of orders placed by the customer
    -->
    <div class="page-head">
        <h1>My Orders</h1>
        <p class="muted">${orders.size()} orders placed</p>
    </div>

    <!--
        This table shows all orders placed by the customer
        Each row shows the order number, date, total amount, status and a details button
    -->
    <div class="table-wrap">
        <table class="data-table">
            <thead>
                <tr>
                    <th>Order #</th>
                    <th>Placed</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>

                <!--
                    This loops through all orders from the database
                    and displays each order in a table row
                -->
                <c:forEach var="order" items="${orders}">
                    <tr>

                        <!-- This shows the unique order id number -->
                        <td>#${order.orderId}</td>

                        <!-- This shows the date when the order was placed -->
                        <td>${order.orderDate}</td>

                        <!-- This shows the total amount of the order in NPR -->
                        <td>NPR ${order.totalAmount}</td>

                        <!--
                            This shows the current status of the order
                            with a colored badge based on the status
                        -->
                        <td>
                            <span class="status ${order.status.toLowerCase()}">
                                ${order.status}
                            </span>
                        </td>

                        <!--
                            This shows the details button for each order
                            Clicking it takes the customer to the order detail page
                        -->
                        <td>
                            <a class="btn btn-small"
                               href="${pageContext.request.contextPath}/orders/view?id=${order.orderId}">
                                Details
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <!--
                    This shows a message if the customer has not placed any orders yet
                -->
                <c:if test="${empty orders}">
                    <tr>
                        <td colspan="5" class="empty-cell">
                            No orders yet.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>