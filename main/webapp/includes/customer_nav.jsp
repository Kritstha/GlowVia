<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header class="site-header">
    <div class="nav-wrap">

         <!--This is the main navigation menu. It contains links to Home, Shop, About and Contact pages-->
        <a class="brand-link" href="${pageContext.request.contextPath}/home">
            <img src="${pageContext.request.contextPath}/images/logo.png"
     alt="Glowvia" style="height: 70px; width: 70px; object-fit: contain; margin-left: -15px;"/>
        </a>

        <!--This checks if the current user has admin role
  If yes it shows the Admin link to access the admin dashboard-->
        <nav class="primary-nav">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/products">Shop</a>
            <a href="${pageContext.request.contextPath}/about">About</a>
            <a href="${pageContext.request.contextPath}/contact">Contact</a>

            <!-- Show admin link only for admin users -->
            <c:if test="${sessionScope.currentUser.role == 'admin'}">
                <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
            </c:if>
        </nav>


        <div class="nav-right">

            <!--This shows the cart link with a badge-->
            <a class="icon-link" href="${pageContext.request.contextPath}/cart" title="Cart">
                Cart
                <span class="cart-badge">${sessionScope.cartCount == null ? 0 : sessionScope.cartCount}</span>
            </a>

            <span class="nav-divider">|</span>


            <span class="${empty sessionScope.currentUser ? 'hide' : ''}"
                  style="display: flex; align-items: center; gap: 14px;">
                <a href="${pageContext.request.contextPath}/profile">
                    Hi, ${sessionScope.currentUser.fullName}
                </a>
                <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </span>

             <!--This section is shown when the user is not logged in. It displays Sign In and Register buttons for the user to login or create account-->
            <span class="${empty sessionScope.currentUser ? '' : 'hide'}"
                  style="display: flex; align-items: center; gap: 14px;">
                <a href="${pageContext.request.contextPath}/login">Sign In</a>
                <a class="btn btn-primary btn-small"
                   href="${pageContext.request.contextPath}/register">Register</a>
            </span>
        </div>
    </div>
</header>