<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header class="site-header">
    <div class="nav-wrap">
        <a class="brand-link" href="${pageContext.request.contextPath}/home">
            <span class="brand-dot"></span>Glowvia
        </a>

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
            <a class="icon-link" href="${pageContext.request.contextPath}/cart" title="Cart">
                Cart
                <span class="cart-badge">${sessionScope.cartCount == null ? 0 : sessionScope.cartCount}</span>
            </a>

            <span class="nav-divider">|</span>

            <%-- Shows when logged IN --%>
            <span class="${empty sessionScope.currentUser ? 'hide' : ''}">
                <a href="${pageContext.request.contextPath}/profile">
                    Hi, ${sessionScope.currentUser.fullName}
                </a>
                <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </span>

            <%-- Shows when logged OUT --%>
            <span class="${empty sessionScope.currentUser ? '' : 'hide'}">
                <a href="${pageContext.request.contextPath}/login">Sign In</a>
                <a class="btn btn-primary btn-small"
                   href="${pageContext.request.contextPath}/register">Register</a>
            </span>
        </div>
    </div>
</header>