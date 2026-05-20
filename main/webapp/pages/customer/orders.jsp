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


    <div class="page-head">
        <h1>My Orders</h1>
        <p class="muted">${orders.size()} orders placed</p>
    </div>


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

                  
                        <td>#${order.orderId}</td>

               
                        <td>${order.orderDate}</td>

 
                        <td>NPR ${order.totalAmount}</td>


                        <td>
                            <span class="status ${order.status.toLowerCase()}">
                                ${order.status}
                            </span>
                        </td>


                        <td>
                            <a class="btn btn-small"
                               href="${pageContext.request.contextPath}/orders/view?id=${order.orderId}">
                                Details
                            </a>
                        </td>
                    </tr>
                </c:forEach>


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


<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>