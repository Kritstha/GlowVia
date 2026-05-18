package com.glowvia.controller;

import com.glowvia.model.User;
import com.glowvia.util.DbConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/admin/brands", "/admin/brand/add", "/admin/brand/delete"})
public class AdminBrandController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when admin visits the brands page
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

        try (Connection conn = DbConfig.getDbConnection()) {

            // Get all brands from the database
            String sql = "SELECT * FROM brands ORDER BY id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Loop through all brands and add them to the list
            List<Map<String, Object>> brands = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> brand = new HashMap<>();
                brand.put("id", rs.getInt("id"));
                brand.put("name", rs.getString("name"));
                brand.put("contact", rs.getString("contact"));
                brands.add(brand);
            }

            // Debugging: print the number of brands fetched
            System.out.println("Brands fetched in controller: " + brands.size());

            // Set the brands list as request attribute
            request.setAttribute("brands", brands);
            request.setAttribute("pageTitle", "Manage Brands - Glowvia");

            // Forward to the brands page
            request.getRequestDispatcher("/pages/admin/brands.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    // This method runs when admin adds or deletes a brand
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

        // Handle add brand request
        if ("/admin/brand/add".equals(path)) {
            String name = request.getParameter("name");
            String contact = request.getParameter("contact");

            // Check if brand name is empty
            if (name == null || name.trim().isEmpty()) {
                session.setAttribute("error", "Brand name is required.");
                response.sendRedirect(request.getContextPath() + "/admin/brands");
                return;
            }

            try (Connection conn = DbConfig.getDbConnection()) {

                // Insert the new brand into the database
                String sql = "INSERT INTO brands (name, contact) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, contact);

                int rows = stmt.executeUpdate();

                // Check if brand was added successfully
                if (rows > 0) {
                    session.setAttribute("success", "Brand added successfully.");
                } else {
                    session.setAttribute("error", "Failed to add brand.");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                session.setAttribute("error", "Something went wrong. Please try again.");
            }
        }

        // Handle delete brand request
        if ("/admin/brand/delete".equals(path)) {
            String brandIdParam = request.getParameter("id");

            try (Connection conn = DbConfig.getDbConnection()) {

                // Delete the brand from the database
                String sql = "DELETE FROM brands WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(brandIdParam));

                int rows = stmt.executeUpdate();

                // Check if brand was deleted successfully
                if (rows > 0) {
                    session.setAttribute("success", "Brand deleted successfully.");
                } else {
                    session.setAttribute("error", "Failed to delete brand.");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                session.setAttribute("error", "Something went wrong. Please try again.");
            }
        }

        // Redirect back to the brands page
        response.sendRedirect(request.getContextPath() + "/admin/brands");
    }
}