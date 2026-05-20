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

        <!-- This is the page heading section
            It shows the title and description of the customers page -->
        <div class="admin-head">
            <h1>Customers</h1>
            <p class="muted">All registered customer accounts.</p>
        </div>

        <!-- This table shows all registered customers in the database
            Admin can view each customer's details like name, email, phone and role -->
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
                    <!-- This loops through all customers from the database
                        and displays each customer in a table row -->
                        
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <!-- This shows the unique id of the customer -->
                            <td>${user.id}</td>

 <!-- This shows the full name of the customer -->
                            <td>${user.fullName}</td>

                            <!-- This shows the username of the customer -->
                            <td>${user.username}</td>

                            <!-- This shows the email address of the customer -->
                            <td>${user.email}</td>

                            <!-- This shows the phone number of the customer -->
                            <td>${user.phone}</td>

                            <!--
                                This shows the role of the customer with a colored badge
                                Regular customers have the user role
                            -->
                            <td>
                                <span class="status ${user.role}">
                                    ${user.role}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>

                    <!--
                        This shows a message if there are no customers
                        registered in the database yet
                    -->
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