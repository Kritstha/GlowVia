<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css"/>
</head>
<body class="admin-body">

<div class="admin-shell">
    <jsp:include page="/includes/admin_sidebar.jsp" />

    <main class="admin-main">
        <jsp:include page="/includes/flash.jsp" />

        <!-- Dashboard heading -->
        <div class="admin-head">
            <h1>Dashboard</h1>
            
        </div>

        <!-- Show success message if any -->
        <c:if test="${not empty sessionScope.success}">
            <div class="toast toast-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session"/>
        </c:if>

        <!-- Show error message if any -->
        <c:if test="${not empty sessionScope.error}">
            <div class="toast toast-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- KPI cards showing store statistics -->
        <div class="kpi-grid">

            <!-- Total products count -->
            <div class="kpi">
                <div class="kpi-label">Total Products</div>
                <div class="kpi-value">${totalProducts}</div>
            </div>

            <!-- Total users count -->
            <div class="kpi">
                <div class="kpi-label">Customers</div>
                <div class="kpi-value">${totalUsers}</div>
            </div>

            <!-- Total orders count -->
            <div class="kpi">
                <div class="kpi-label">Orders Placed</div>
                <div class="kpi-value">${totalOrders}</div>
            </div>
        </div>

        <!-- Recent users table -->
        <div class="admin-head" style="margin-top: 30px;">
            <h2>Recent Customers</h2>
        </div>

        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Loop through all users and display them -->
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.fullName}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.phone}</td>
                        </tr>
                    </c:forEach>

                    <!-- Show message if no users found -->
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="5" class="empty-cell">
                                No customers yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Quick action buttons -->
        <div class="admin-quicklinks" style="margin-top: 20px;">
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/admin/manage-product">
                Manage Products
            </a>
            <a class="btn" href="${pageContext.request.contextPath}/admin/orders">
                Manage Orders
            </a>
            <a class="btn" href="${pageContext.request.contextPath}/admin/brands">
                Manage Brands
            </a>
        </div>
    </main>
</div>

</body>
</html>