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

/*
  This controller handles all admin product management operations
  Admin can view all products, add new products, edit existing products and delete products
 */
@WebServlet({"/admin/manage-product", "/admin/products", "/admin/add-product", "/admin/edit-product", "/admin/delete-product"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,   
    maxFileSize = 1024 * 1024 * 10,         
    maxRequestSize = 1024 * 1024 * 50       
)
public class AdminProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

       
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

      
        if (!"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        
        String path = request.getServletPath();

        /*
         This block shows the add product form
          It forwards to the product form JSP with an empty product
         */
        if ("/admin/add-product".equals(path)) {
            request.setAttribute("pageTitle", "Add Product - Glowvia");
            request.getRequestDispatcher("/pages/admin/product_form.jsp").forward(request, response);
            return;
        }

        /*
          This block shows the edit product form
         It gets the product from the database and passes it to the form JSP
         */
        if ("/admin/edit-product".equals(path)) {
            String productIdParam = request.getParameter("id");

           
            if (productIdParam == null || productIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/manage-product");
                return;
            }

            try (Connection conn = DbConfig.getDbConnection()) {

                
                String sql = "SELECT * FROM products WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(productIdParam));
                ResultSet rs = stmt.executeQuery();

               
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

        /*
          This block shows all products in the database
         It gets all products with their brand names and forwards to the products JSP
         */
        try (Connection conn = DbConfig.getDbConnection()) {

           
            String sql = "SELECT p.*, b.name as brand_name FROM products p "
                       + "LEFT JOIN brands b ON p.brand_id = b.id "
                       + "ORDER BY p.id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

           
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
          
            System.out.println("Products fetched in controller: " + products.size());

           
            request.setAttribute("products", products);
            request.setAttribute("pageTitle", "Manage Products - Glowvia");
            request.getRequestDispatcher("/pages/admin/products.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    /*
      This method runs when admin submits the add or edit product form
      It also handles the delete product request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        
        String path = request.getServletPath();

        /*
          This block handles the delete product request
          It gets the product id from the form and deletes it from the database
         */
        if ("/admin/delete-product".equals(path)) {
            String productIdParam = request.getParameter("id");

            try (Connection conn = DbConfig.getDbConnection()) {

           
                String sql = "DELETE FROM products WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(productIdParam));
                int rows = stmt.executeUpdate();

              
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

        /*
          This block handles the add and edit product requests
          It gets all form fields and either inserts a new product or updates existing one
         */
        try {

            String productIdParam = request.getParameter("id");
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            String skinType = request.getParameter("skin_type");
            String keyIngredients = request.getParameter("key_ingredients");
            double price = Double.parseDouble(request.getParameter("price"));
            int stockQuantity = Integer.parseInt(request.getParameter("stock_quantity"));
            String description = request.getParameter("description");


            String imagePath = handleImageUpload(request, name);

            try (Connection conn = DbConfig.getDbConnection()) {

                /*
                 If product id exists it means admin is editing an existing product
                 If product id is empty it means admin is adding a new product
                 */
                if (productIdParam != null && !productIdParam.isEmpty()) {

                   
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
        response.sendRedirect(request.getContextPath() + "/admin/manage-product");
    }

    /*
      This method handles uploading the product image to the server
      It saves the image in the uploads/products folder
      If no image is uploaded it returns null
     */
    private String handleImageUpload(HttpServletRequest request, String productName)
            throws IOException, ServletException {
        Part filePart = request.getPart("photo");

       
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        String uploadPath = request.getServletContext().getRealPath("/uploads/products");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = productName + "_Photo" + getFileExtension(filePart.getSubmittedFileName());
        filePart.write(uploadPath + File.separator + fileName);

        return "uploads/products/" + fileName;
    }

    /*
      This method gets the file extension from the uploaded image filename
      If no extension is found it returns .png as the default extension
     */
    private String getFileExtension(String fileName) {
        return fileName == null || !fileName.contains(".") ?
               ".png" : fileName.substring(fileName.lastIndexOf("."));
    }
}