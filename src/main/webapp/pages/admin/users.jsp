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

        <!-- Page heading -->
        <div class="admin-head">
            <h1>Customers</h1>
            <p class="muted">All registered customer accounts.</p>
        </div>

        <!-- Users table -->
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Loop through all users and display them -->
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <!-- User id -->
                            <td>${user.id}</td>

                            <!-- User full name -->
                            <td>${user.fullName}</td>

                            <!-- Username -->
                            <td>${user.username}</td>

                            <!-- User email -->
                            <td>${user.email}</td>

                            <!-- User phone -->
                            <td>${user.phone}</td>

                            <!-- User role -->
                            <td>
                                <span class="status ${user.role}">
                                    ${user.role}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Show message if no users found -->
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="6" class="empty-cell">
                                No customers yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</div>

</body>
</html>