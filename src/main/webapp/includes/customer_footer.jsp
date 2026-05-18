<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<footer class="site-footer">
    <div class="footer-grid">
        <div>
            <h4>Glowvia</h4>
            <p class="muted">Clean, conscious beauty for everyday radiance.</p>
        </div>
        <div>
            <h4>Shop</h4>
            <a href="${pageContext.request.contextPath}/products">All Products</a><br/>
            <a href="${pageContext.request.contextPath}/products?category=Serum">Serums</a><br/>
            <a href="${pageContext.request.contextPath}/products?category=Moisturizer">Moisturizers</a><br/>
            <a href="${pageContext.request.contextPath}/products?category=Cleanser">Cleansers</a>
        </div>
        <div>
            <h4>Company</h4>
            <a href="${pageContext.request.contextPath}/about">About Us</a><br/>
            <a href="${pageContext.request.contextPath}/contact">Contact</a>
        </div>
        <div>
            <h4>Account</h4>
            <a href="${pageContext.request.contextPath}/login">Sign In</a><br/>
            <a href="${pageContext.request.contextPath}/register">Create Account</a><br/>
            <a href="${pageContext.request.contextPath}/orders">My Orders</a>
        </div>
    </div>
    <div class="copyright">
        &copy; 2026 Glowvia Beauty Co. - All rights reserved.
    </div>
</footer>
