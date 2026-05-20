package com.glowvia.service;

import com.glowvia.util.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewService {

    // This method gets all reviews for a specific product from the database
    public List<Map<String, Object>> getReviewsByProductId(int productId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name FROM reviews r "
                   + "JOIN users u ON r.user_id = u.id "
                   + "WHERE r.product_id = ? "
                   + "ORDER BY r.created_at DESC";

        try (Connection conn = DbConfig.getDbConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            // Loop through all reviews and add them to the list
            while (rs.next()) {
                Map<String, Object> review = new HashMap<>();
                review.put("id", rs.getInt("id"));
                review.put("rating", rs.getInt("rating"));
                review.put("comment", rs.getString("comment"));
                review.put("fullName", rs.getString("full_name"));
                review.put("createdAt", rs.getTimestamp("created_at"));
                reviews.add(review);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // This method adds a new review to the database
    public boolean addReview(int productId, int userId, int rating, String comment) {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.setInt(2, userId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method checks if a user has already reviewed a product
    public boolean hasUserReviewed(int productId, int userId) {
        String sql = "SELECT id FROM reviews WHERE product_id = ? AND user_id = ?";

        try (Connection conn = DbConfig.getDbConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}