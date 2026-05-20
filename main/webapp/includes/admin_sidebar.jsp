<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<aside class="admin-sidebar">
<!-- This is the admin brand logo that links to the dashboard -->
    <a class="admin-brand" href="${pageContext.request.contextPath}/admin/dashboard">
        <span class="brand-dot"></span>Glowvia Admin
    </a>
<!-- This is the admin navigation menu with links to all admin pages -->
    <nav class="admin-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/manage-product">Products</a>
        <a href="${pageContext.request.contextPath}/admin/brands">Brands</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/users">Customers</a>
        <a href="${pageContext.request.contextPath}/admin/reviews">Reviews</a>
    </nav>
 <!-- This shows the logged in admin name with logout and view store buttons -->
    <div class="admin-foot">
        <div class="muted">Signed in as</div>
        <div><strong>${sessionScope.currentUser.fullName}</strong></div>
        <a class="btn btn-small" href="${pageContext.request.contextPath}/logout">Logout</a>
        <a class="btn btn-small" href="${pageContext.request.contextPath}/home">View store</a>
    </div>
</aside>