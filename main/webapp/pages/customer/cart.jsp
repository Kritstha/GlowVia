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
        <h1>Your Cart</h1>
        <p class="muted">${cartItems.size()} items in cart</p>
    </div>

    <!--
        This table shows all the items that are currently in the cart
        Each row shows the product image, name, price, quantity and line total
    -->
    <div class="table-wrap">
        <table class="data-table">
            <thead>
                <tr>
                    <th>Photo</th>
                    <th>Product</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Line Total</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>

         
                <c:forEach var="item" items="${cartItems}">
                    <tr>

        
                        <td>
                            <img class="row-thumb"
                                 src="${pageContext.request.contextPath}/${item.get('photoPath')}"
                                 alt="${item.get('name')}"
                                 onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
                        </td>


                        <td>${item.get('name')}</td>


                        <td>NPR ${item.get('price')}</td>


                        <td>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/cart/update"
                                  class="inline-form">
                                <input type="hidden" name="cartItemId"
                                       value="${item.get('cartItemId')}"/>
                                <input type="number" name="quantity" min="1"
                                       value="${item.get('quantity')}" class="qty-input"/>
                                <button class="btn btn-small" type="submit">Update</button>
                            </form>
                        </td>


                        <td>NPR ${item.get('price') * item.get('quantity')}</td>


                        <td>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/cart/remove"
                                  class="inline-form">
                                <input type="hidden" name="cartItemId"
                                       value="${item.get('cartItemId')}"/>
                                <button class="btn btn-small btn-danger" type="submit">
                                    Remove
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

 
                <c:if test="${empty cartItems}">
                    <tr>
                        <td colspan="6" class="empty-cell">
                            Your cart is empty.
                            <a href="${pageContext.request.contextPath}/products">
                                Browse our collection
                            </a>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>


    <c:if test="${not empty cartItems}">
        <div class="cart-summary">


            <div class="cart-total">
                <span>Order total</span>
                <strong>NPR ${total_amount}</strong>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/checkout">
                <button type="submit" class="btn btn-primary btn-block">
                    Place Order
                </button>
            </form>
        </div>
    </c:if>
</section>

<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>