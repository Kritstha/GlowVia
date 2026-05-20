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
        It shows the cart title and the number of items in the cart
    -->
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

                <!--
                    This loops through all cart items
                    and displays each item in a table row
                -->
                <c:forEach var="item" items="${cartItems}">
                    <tr>

                        <!--
                            This shows the product thumbnail image
                            If no image is found it shows a placeholder image
                        -->
                        <td>
                            <img class="row-thumb"
                                 src="${pageContext.request.contextPath}/${item.get('photoPath')}"
                                 alt="${item.get('name')}"
                                 onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
                        </td>

                        <!-- This shows the name of the product -->
                        <td>${item.get('name')}</td>

                        <!-- This shows the price of the product in NPR -->
                        <td>NPR ${item.get('price')}</td>

                        <!--
                            This is the quantity update form
                            Customer can change the quantity and click Update to save it
                        -->
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

                        <!-- This shows the total price for this item which is price multiplied by quantity -->
                        <td>NPR ${item.get('price') * item.get('quantity')}</td>

                        <!--
                            This is the remove button for the cart item
                            Customer clicks this to remove the item from the cart
                        -->
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

                <!--
                    This shows a message if the cart is empty
                    It also shows a link to browse the product collection
                -->
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

    <!--
        This section shows the order total and place order button
        It is only shown when there are items in the cart
    -->
    <c:if test="${not empty cartItems}">
        <div class="cart-summary">

            <!--
                This shows the total amount of all items in the cart
                The total is calculated by adding up all the line totals
            -->
            <div class="cart-total">
                <span>Order total</span>
                <strong>NPR ${total_amount}</strong>
            </div>

            <!--
                This is the place order button
                Customer clicks this to proceed to checkout and place the order
            -->
            <form method="post" action="${pageContext.request.contextPath}/checkout">
                <button type="submit" class="btn btn-primary btn-block">
                    Place Order
                </button>
            </form>
        </div>
    </c:if>
</section>

<!--
    This includes the customer footer at the bottom of the page
    It shows links, contact details and the Google Maps location
-->
<jsp:include page="/includes/customer_footer.jsp"/>

</body>
</html>
