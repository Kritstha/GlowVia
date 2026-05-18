package com.glowvia.service;

import com.glowvia.model.User;
import com.glowvia.util.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterService {

    // Database connection object
    private Connection dbConn;

    // This constructor opens the database connection when service is created
    public RegisterService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
            if (this.dbConn == null) {
                throw new RuntimeException("Failed to establish database connection");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Database initialization failed", ex);
        }
    }

    // This method adds a new user to the database
    public boolean addUser(User user) {
        if (user == null) {
            System.err.println("User is null");
            return false;
        }

        // SQL query to insert new user into users table
        String sql = "INSERT INTO users (full_name, username, email, phone, dob, gender, password, image_path, user_role) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'user')";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            // Set all the user details in the query
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getDob());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPassword());
            stmt.setString(8, user.getImagePath());

            // Execute the query and check if row was inserted
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected <= 0) {
                System.err.println("No rows affected by insert");
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Print detailed error information if something goes wrong
            System.err.println("SQL Error during registration:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            return false;
        }
    }

    // This method checks if the username is already taken in the database
    public boolean isUsernameTaken(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        // Search for the username in the database
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Error checking username availability");
            return false;
        }
    }

    // This method checks if the email is already registered
    public boolean isEmailTaken(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Search for the email in the database
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Error checking email availability");
            return false;
        }
    }

    // This method checks if the phone number is already registered
    public boolean isPhoneTaken(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // Search for the phone number in the database
        String sql = "SELECT id FROM users WHERE phone = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Error checking phone availability");
            return false;
        }
    }
}