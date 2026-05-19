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

@WebServlet({"/admin/reviews", "/admin/review/delete"})
public class AdminReviewController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when admin visits the reviews page
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

            // Get all reviews from the database with product and user details
            String sql = "SELECT r.id, r.rating, r.comment, r.created_at, "
                       + "u.full_name, p.name as product_name "
                       + "FROM reviews r "
                       + "JOIN users u ON r.user_id = u.id "
                       + "JOIN products p ON r.product_id = p.id "
                       + "ORDER BY r.created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Loop through all reviews and add them to the list
            List<Map<String, Object>> reviews = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> review = new HashMap<>();
                review.put("id", rs.getInt("id"));
                review.put("rating", rs.getInt("rating"));
                review.put("comment", rs.getString("comment"));
                review.put("fullName", rs.getString("full_name"));
                review.put("productName", rs.getString("product_name"));
                review.put("createdAt", rs.getTimestamp("created_at"));
                reviews.add(review);
            }

            // Debugging print number of reviews fetched
            System.out.println("Reviews fetched: " + reviews.size());

            // Set reviews as request attribute
            request.setAttribute("reviews", reviews);
            request.setAttribute("pageTitle", "Reviews - Admin");

            // Forward to reviews page
            request.getRequestDispatcher("/pages/admin/reviews.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    // This method runs when admin deletes a review
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

        // Get the review id from the form
        String reviewIdParam = request.getParameter("id");

        try (Connection conn = DbConfig.getDbConnection()) {

            // Delete the review from the database
            String sql = "DELETE FROM reviews WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(reviewIdParam));
            int rows = stmt.executeUpdate();

            // Check if review was deleted successfully
            if (rows > 0) {
                session.setAttribute("success", "Review deleted successfully.");
            } else {
                session.setAttribute("error", "Failed to delete review.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            session.setAttribute("error", "Something went wrong. Please try again.");
        }

        // Redirect back to reviews page
        response.sendRedirect(request.getContextPath() + "/admin/reviews");
    }
}