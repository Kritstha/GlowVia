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
            <h1>Brands</h1>
            <p class="muted">Add, view and remove product brands.</p>
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

        <div class="two-col">

            <!-- Add new brand form -->
            <div class="card">
                <h2>Add a new brand</h2>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin/brand/add"
                      class="form">

                    <!-- Brand name field -->
                    <div class="form-row">
                        <label for="brandName">Brand name</label>
                        <input id="brandName" name="name" type="text"
                               required maxlength="255"/>
                    </div>

                    <!-- Brand contact field -->
                    <div class="form-row">
                        <label for="brandContact">Contact info</label>
                        <input id="brandContact" name="contact" type="text"
                               maxlength="500"/>
                    </div>

                    <button type="submit" class="btn btn-primary">Add brand</button>
                </form>
            </div>

            <!-- Existing brands table -->
            <div class="card">
                <h2>Existing brands</h2>
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Contact</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Loop through all brands and display them -->
                            <c:forEach var="brand" items="${brands}">
                                <tr>
                                    <!-- Brand id -->
                                    <td>${brand.get('id')}</td>

                                    <!-- Brand name -->
                                    <td>${brand.get('name')}</td>

                                    <!-- Brand contact -->
                                    <td>${brand.get('contact')}</td>

                                    <!-- Delete button -->
                                    <td>
                                        <form class="inline-form" method="post"
                                              action="${pageContext.request.contextPath}/admin/brand/delete"
                                              onsubmit="return confirm('Delete this brand?');">
                                            <input type="hidden" name="id"
                                                   value="${brand.get('id')}"/>
                                            <button class="btn btn-small btn-danger"
                                                    type="submit">
                                                Delete
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>

                            <!-- Show message if no brands found -->
                            <c:if test="${empty brands}">
                                <tr>
                                    <td colspan="4" class="empty-cell">
                                        No brands yet.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
</div>

</body>
</html>