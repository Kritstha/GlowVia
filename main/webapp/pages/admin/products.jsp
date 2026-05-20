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

        <!-- This is the page heading with add product button -->
        <div class="admin-head">
            <h1>Products</h1>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/admin/add-product">
                + Add Product
            </a>
        </div>

        <!-- This table shows all products in the database -->
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Name</th>
                        <th>Brand</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- This loops through all products and displays each one -->
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>
                                <img class="row-thumb"
                                     src="${pageContext.request.contextPath}/${product.photoPath}"
                                     alt="${product.name}"
                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/placeholder.png'"/>
                            </td>
                            <td>${product.name}</td>
                            <td>${product.brandName}</td>
                            <td>${product.category}</td>
                            <td>NPR ${product.price}</td>
                            <td>
                                ${product.stockQuantity}
                                <c:if test="${product.stockQuantity <= 0}">
                                    <span class="badge bad">Out</span>
                                </c:if>
                                <c:if test="${product.stockQuantity > 0 && product.stockQuantity <= 5}">
                                    <span class="badge warn">Low</span>
                                </c:if>
                                <c:if test="${product.stockQuantity > 5}">
                                    <span class="badge ok">OK</span>
                                </c:if>
                            </td>
                            <td class="action-cell">
                                <a class="btn btn-small"
                                   href="${pageContext.request.contextPath}/admin/edit-product?id=${product.id}">
                                    Edit
                                </a>
                                <form class="inline-form" method="post"
                                      action="${pageContext.request.contextPath}/admin/delete-product"
                                      onsubmit="return confirm('Delete this product?');">
                                    <input type="hidden" name="id" value="${product.id}"/>
                                    <button class="btn btn-small btn-danger" type="submit">
                                        Delete
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- This shows message if no products found -->
                    <c:if test="${empty products}">
                        <tr>
                            <td colspan="7" class="empty-cell">
                                No products yet. Click Add Product to create one.
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