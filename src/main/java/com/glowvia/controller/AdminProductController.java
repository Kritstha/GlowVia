package com.glowvia.controller;

import com.glowvia.model.Product;
import com.glowvia.model.User;
import com.glowvia.util.DbConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/admin/manage-product", "/admin/products", "/admin/add-product", "/admin/edit-product", "/admin/delete-product"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
    maxFileSize = 1024 * 1024 * 10,         // 10MB
    maxRequestSize = 1024 * 1024 * 50       // 50MB
)
public class AdminProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when admin visits the manage products page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current user from the session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // If user is not admin redirect to home page
        if (!"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Get the requested path
        String path = request.getServletPath();

        // Show the add product form
        if ("/admin/add-product".equals(path)) {
            request.setAttribute("pageTitle", "Add Product - Glowvia");
            request.getRequestDispatcher("/pages/admin/product_form.jsp").forward(request, response);
            return;
        }

        // Show the edit product form
        if ("/admin/edit-product".equals(path)) {
            String productIdParam = request.getParameter("id");
            if (productIdParam == null || productIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/manage-product");
                return;
            }

            try (Connection conn = DbConfig.getDbConnection()) {
                // Get the product details from the database
                String sql = "SELECT * FROM products WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(productIdParam));
                ResultSet rs = stmt.executeQuery();

                // If product found pass it to the edit form
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setSkinType(rs.getString("skin_type"));
                    product.setKeyIngredients(rs.getString("key_ingredients"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStockQuantity(rs.getInt("stock_quantity"));
                    product.setDescription(rs.getString("description"));
                    product.setPhotoPath(rs.getString("photo_path"));

                    request.setAttribute("product", product);
                    request.setAttribute("pageTitle", "Edit Product - Glowvia");
                    request.getRequestDispatcher("/pages/admin/product_form.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/manage-product");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/manage-product");
            }
            return;
        }

        // Default: show all products
        try (Connection conn = DbConfig.getDbConnection()) {

            // Get all products from the database
            String sql = "SELECT p.*, b.name as brand_name FROM products p "
                       + "LEFT JOIN brands b ON p.brand_id = b.id "
                       + "ORDER BY p.id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Loop through all products and add them to the list
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setPhotoPath(rs.getString("photo_path"));
                products.add(product);
            }

            // Debugging: print the number of products fetched
            System.out.println("Products fetched in controller: " + products.size());

            // Set the product list as request attribute
            request.setAttribute("products", products);
            request.setAttribute("pageTitle", "Manage Products - Glowvia");

            // Forward to the manage products page
            request.getRequestDispatcher("/pages/admin/products.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    // This method runs when admin submits add or edit product form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current user from the session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get the requested path
        String path = request.getServletPath();

        // Handle delete product request
        if ("/admin/delete-product".equals(path)) {
            String productIdParam = request.getParameter("id");

            try (Connection conn = DbConfig.getDbConnection()) {
                // Delete the product from the database
                String sql = "DELETE FROM products WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(productIdParam));
                int rows = stmt.executeUpdate();

                // Check if product was deleted successfully
                if (rows > 0) {
                    session.setAttribute("success", "Product deleted successfully.");
                } else {
                    session.setAttribute("error", "Failed to delete product.");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                session.setAttribute("error", "Something went wrong. Please try again.");
            }

            response.sendRedirect(request.getContextPath() + "/admin/manage-product");
            return;
        }

        // Handle add or edit product request
        try {
            // Get all the form fields
            String productIdParam = request.getParameter("id");
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            String skinType = request.getParameter("skin_type");
            String keyIngredients = request.getParameter("key_ingredients");
            double price = Double.parseDouble(request.getParameter("price"));
            int stockQuantity = Integer.parseInt(request.getParameter("stock_quantity"));
            String description = request.getParameter("description");

            // Handle product image upload
            String imagePath = handleImageUpload(request, name);

            try (Connection conn = DbConfig.getDbConnection()) {

                // If product id exists update the product otherwise add new product
                if (productIdParam != null && !productIdParam.isEmpty()) {

                    // Update existing product in the database
                    String sql;
                    PreparedStatement stmt;

                    if (imagePath != null) {
                        sql = "UPDATE products SET name=?, category=?, skin_type=?, key_ingredients=?, price=?, stock_quantity=?, description=?, photo_path=? WHERE id=?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, name);
                        stmt.setString(2, category);
                        stmt.setString(3, skinType);
                        stmt.setString(4, keyIngredients);
                        stmt.setDouble(5, price);
                        stmt.setInt(6, stockQuantity);
                        stmt.setString(7, description);
                        stmt.setString(8, imagePath);
                        stmt.setInt(9, Integer.parseInt(productIdParam));
                    } else {
                        sql = "UPDATE products SET name=?, category=?, skin_type=?, key_ingredients=?, price=?, stock_quantity=?, description=? WHERE id=?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, name);
                        stmt.setString(2, category);
                        stmt.setString(3, skinType);
                        stmt.setString(4, keyIngredients);
                        stmt.setDouble(5, price);
                        stmt.setInt(6, stockQuantity);
                        stmt.setString(7, description);
                        stmt.setInt(8, Integer.parseInt(productIdParam));
                    }

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        session.setAttribute("success", "Product updated successfully.");
                    } else {
                        session.setAttribute("error", "Failed to update product.");
                    }

                } else {
                    // Insert new product into the database
                    String sql = "INSERT INTO products (name, category, skin_type, key_ingredients, price, stock_quantity, description, photo_path) "
                               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setString(2, category);
                    stmt.setString(3, skinType);
                    stmt.setString(4, keyIngredients);
                    stmt.setDouble(5, price);
                    stmt.setInt(6, stockQuantity);
                    stmt.setString(7, description);
                    stmt.setString(8, imagePath);

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        session.setAttribute("success", "Product added successfully.");
                    } else {
                        session.setAttribute("error", "Failed to add product.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Error: " + e.getMessage());
        }

        // Redirect back to manage products page
        response.sendRedirect(request.getContextPath() + "/admin/manage-product");
    }

    // This method handles uploading the product image to the server
    private String handleImageUpload(HttpServletRequest request, String productName)
            throws IOException, ServletException {
        Part filePart = request.getPart("photo");

        // If no image uploaded return null
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        // Get the folder path where product images will be stored
        String uploadPath = request.getServletContext().getRealPath("/uploads/products");
        File uploadDir = new File(uploadPath);

        // Create the folder if it does not exist
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the image with the product name as the filename
        String fileName = productName + "_Photo" + getFileExtension(filePart.getSubmittedFileName());
        filePart.write(uploadPath + File.separator + fileName);

        return "uploads/products/" + fileName;
    }

    // This method gets the file extension from the image filename
    private String getFileExtension(String fileName) {
        return fileName == null || !fileName.contains(".") ?
               ".png" : fileName.substring(fileName.lastIndexOf("."));
    }
}