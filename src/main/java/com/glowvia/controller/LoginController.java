package com.glowvia.controller;
import com.glowvia.util.DbConfig;

import com.glowvia.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This controller handles the login page
@WebServlet("/login")
public class LoginController extends HttpServlet {

    // This method shows the login page when user visits /login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
    }

    // This method runs when user submits the login form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the username/email and password from the login form
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        // Check if the fields are empty
        if (identifier == null || identifier.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter username and password.");
            request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
            return;
        }

        // Connect to the database
        try (Connection conn = DbConfig.getDbConnection()) {

            // Search for the user by username or email
            String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, identifier.trim());
            ps.setString(2, identifier.trim());
            ResultSet rs = ps.executeQuery();

            // Check if user was found in the database
            if (rs.next()) {

                // Get the stored password hash from database
                String storedPassword = rs.getString("password");

                // Compare the entered password with the stored hash using BCrypt
                if (BCrypt.checkpw(password, storedPassword)) {

                    // Password matches - create user object with their details
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("user_role"));

                    // Save the user in the session so they stay logged in
                    HttpSession session = request.getSession();
                    session.setAttribute("currentUser", user);

                    // Redirect admin to dashboard, regular users to home page
                    if ("admin".equals(user.getRole())) {
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
                    }

                } else {
                    // Password does not match - show error message
                    request.setAttribute("error", "Incorrect password. Please try again.");
                    request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
                }

            } else {
                // No user found with that username or email
                request.setAttribute("error", "No account found for that username/email.");
                request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            // Something went wrong with the database connection
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
        }
    }
}