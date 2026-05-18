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

        <!-- Page heading with back button -->
        <div class="admin-head">
            <c:choose>
                <c:when test="${not empty product.id}">
                    <h1>Edit Product</h1>
                </c:when>
                <c:otherwise>
                    <h1>Add Product</h1>
                </c:otherwise>
            </c:choose>
            <a class="btn" href="${pageContext.request.contextPath}/admin/manage-product">
                &larr; Back to products
            </a>
        </div>

        <!-- Show error message if any -->
        <c:if test="${not empty sessionScope.error}">
            <div class="toast toast-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="card">
            <!-- Add or edit product form -->
            <form method="post"
                  action="${pageContext.request.contextPath}${not empty product.id ? '/admin/edit-product' : '/admin/add-product'}"
                  enctype="multipart/form-data"
                  class="form form-grid">

                <!-- Hidden product id for edit -->
                <input type="hidden" name="id" value="${product.id}"/>

                <!-- Product name field -->
                <div class="form-row">
                    <label for="name">Product name</label>
                    <input id="name" name="name" type="text" required maxlength="150"
                           value="${product.name}"/>
                </div>

                <!-- Product category field -->
                <div class="form-row">
                    <label for="category">Category</label>
                    <input id="category" name="category" type="text" required maxlength="100"
                           value="${product.category}" list="categoryOptions"/>
                    <datalist id="categoryOptions">
                        <option value="Serum"></option>
                        <option value="Moisturizer"></option>
                        <option value="Cleanser"></option>
                        <option value="Mask"></option>
                        <option value="Toner"></option>
                        <option value="Sunscreen"></option>
                        <option value="Eye Care"></option>
                        <option value="Lip Care"></option>
                    </datalist>
                </div>

                <!-- Skin type dropdown -->
                <div class="form-row">
                    <label for="skin_type">Skin type</label>
                    <select id="skin_type" name="skin_type">
                        <option value="All" ${product.skinType == 'All' ? 'selected' : ''}>All</option>
                        <option value="Oily" ${product.skinType == 'Oily' ? 'selected' : ''}>Oily</option>
                        <option value="Dry" ${product.skinType == 'Dry' ? 'selected' : ''}>Dry</option>
                        <option value="Combination" ${product.skinType == 'Combination' ? 'selected' : ''}>Combination</option>
                        <option value="Sensitive" ${product.skinType == 'Sensitive' ? 'selected' : ''}>Sensitive</option>
                        <option value="Mature" ${product.skinType == 'Mature' ? 'selected' : ''}>Mature</option>
                    </select>
                </div>

                <!-- Product price field -->
                <div class="form-row">
                    <label for="price">Price (NPR)</label>
                    <input id="price" name="price" type="number" step="0.01"
                           min="0.01" required value="${product.price}"/>
                </div>

                <!-- Stock quantity field -->
                <div class="form-row">
                    <label for="stock_quantity">Stock quantity</label>
                    <input id="stock_quantity" name="stock_quantity" type="number"
                           min="0" required value="${product.stockQuantity}"/>
                </div>

                <!-- Key ingredients field -->
                <div class="form-row form-row-full">
                    <label for="key_ingredients">Key ingredients</label>
                    <input id="key_ingredients" name="key_ingredients" type="text"
                           maxlength="500" value="${product.keyIngredients}"/>
                </div>

                <!-- Description field -->
                <div class="form-row form-row-full">
                    <label for="description">Description</label>
                    <textarea id="description" name="description"
                              rows="4">${product.description}</textarea>
                </div>

                <!-- Product image upload field -->
                <div class="form-row form-row-full">
                    <label for="photo">
                        Product image
                        <c:if test="${not empty product.id}">
                            (leave empty to keep current)
                        </c:if>
                    </label>
                    <input id="photo" name="photo" type="file" accept="image/*"/>

                    <!-- Show current image path if editing -->
                    <c:if test="${not empty product.photoPath}">
                        <div class="muted small">
                            Current: ${product.photoPath}
                        </div>
                    </c:if>
                </div>

                <!-- Submit button -->
                <div class="form-row form-row-full">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${not empty product.id}">Save changes</c:when>
                            <c:otherwise>Add product</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </main>
</div>

</body>
</html>