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

    <!--
        This includes the admin sidebar navigation
        It shows all the admin menu links on the left side
    -->
    <jsp:include page="/includes/admin_sidebar.jsp" />

    <main class="admin-main">

        <!--
            This includes the flash messages section
            It shows success or error messages after an action
        -->
        <jsp:include page="/includes/flash.jsp" />

        <!--
            This is the page heading section
            It shows Add Product if adding a new product
            or Edit Product if editing an existing product
        -->
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

        <!--
            This checks if there is an error message in the session
            If yes it shows a red toast message and removes it from session
        -->
        <c:if test="${not empty sessionScope.error}">
            <div class="toast toast-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="card">

            <!--
                This is the add or edit product form
                If product id exists it submits to edit product
                If product id is empty it submits to add product
            -->
            <form method="post"
                  action="${pageContext.request.contextPath}${not empty product.id ? '/admin/edit-product' : '/admin/add-product'}"
                  enctype="multipart/form-data"
                  class="form form-grid">

                <!--
                    This hidden field stores the product id
                    It is used when editing an existing product
                -->
                <input type="hidden" name="id" value="${product.id}"/>

                <!--
                    This is the product name input field
                    Admin must enter a name for the product
                -->
                <div class="form-row">
                    <label for="name">Product name</label>
                    <input id="name" name="name" type="text" required maxlength="150"
                           value="${product.name}"/>
                </div>

                <!--
                    This is the product category input field
                    Admin can type or select from the dropdown list of categories
                -->
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

                <!--
                    This is the skin type dropdown
                    Admin selects which skin type the product is suitable for
                -->
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

                <!--
                    This is the product price input field
                    Admin enters the price of the product in NPR
                -->
                <div class="form-row">
                    <label for="price">Price (NPR)</label>
                    <input id="price" name="price" type="number" step="0.01"
                           min="0.01" required value="${product.price}"/>
                </div>

                <!--
                    This is the stock quantity input field
                    Admin enters how many units of the product are available
                    Minimum value is 0 to prevent negative stock
                -->
                <div class="form-row">
                    <label for="stock_quantity">Stock quantity</label>
                    <input id="stock_quantity" name="stock_quantity" type="number"
                           min="0" required value="${product.stockQuantity}"/>
                </div>

                <!--
                    This is the key ingredients input field
                    Admin enters the main ingredients of the product
                -->
                <div class="form-row form-row-full">
                    <label for="key_ingredients">Key ingredients</label>
                    <input id="key_ingredients" name="key_ingredients" type="text"
                           maxlength="500" value="${product.keyIngredients}"/>
                </div>

                <!--
                    This is the product description textarea
                    Admin enters a detailed description of the product
                -->
                <div class="form-row form-row-full">
                    <label for="description">Description</label>
                    <textarea id="description" name="description"
                              rows="4">${product.description}</textarea>
                </div>

                <!--
                    This is the product image upload field
                    Admin can upload a new image for the product
                    When editing leaving this empty keeps the current image
                -->
                <div class="form-row form-row-full">
                    <label for="photo">
                        Product image
                        <c:if test="${not empty product.id}">
                            (leave empty to keep current)
                        </c:if>
                    </label>
                    <input id="photo" name="photo" type="file" accept="image/*"/>

                    <!--
                        This shows the current image path when editing a product
                        So admin knows which image is currently being used
                    -->
                    <c:if test="${not empty product.photoPath}">
                        <div class="muted small">
                            Current: ${product.photoPath}
                        </div>
                    </c:if>
                </div>

                <!--
                    This is the submit button
                    It shows Save changes when editing or Add product when adding
                -->
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