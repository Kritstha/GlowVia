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

/*
 This controller handles the login page
 Customer can login using their username or email and password
*/
@WebServlet("/login")
public class LoginController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        // Check if username and password fields are empty
        if (identifier == null || identifier.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter username and password.");
            request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DbConfig.getDbConnection()) {

            // Search for user by username or email in the database
            String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, identifier.trim());
            ps.setString(2, identifier.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String storedPassword = rs.getString("password");

                /*
                 Check if entered password matches the stored BCrypt hash
                 If it matches create a session and redirect based on role
                */
                if (BCrypt.checkpw(password, storedPassword)) {

                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("user_role"));

                    HttpSession session = request.getSession();
                    session.setAttribute("currentUser", user);

                    // Admin goes to dashboard, regular user goes to home page
                    if ("admin".equals(user.getRole())) {
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
                    }

                } else {
                    request.setAttribute("error", "Incorrect password. Please try again.");
                    request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
                }

            } else {
                request.setAttribute("error", "No account found for that username/email.");
                request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher("/pages/customer/login.jsp").forward(request, response);
        }
    }
}