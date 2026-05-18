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

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when user visits the profile page
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

        try (Connection conn = DbConfig.getDbConnection()) {

            // Get the latest user details from the database
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();

            // If user found pass their details to the profile page
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDob(rs.getString("dob"));
                user.setGender(rs.getString("gender"));
                user.setImagePath(rs.getString("image_path"));
                user.setRole(rs.getString("user_role"));

                // Pass the user to the profile page
                request.setAttribute("user", user);
                request.setAttribute("pageTitle", "My Profile - Glowvia");
                request.getRequestDispatcher("/pages/customer/profile.jsp").forward(request, response);
            } else {
                // If user not found redirect to home page
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    // This method runs when user submits the update profile form
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

        // Get the updated details from the form
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");

        try (Connection conn = DbConfig.getDbConnection()) {

            // Update the user details in the database
            String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, dob = ?, gender = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, dob);
            ps.setString(5, gender);
            ps.setInt(6, currentUser.getId());

            int rows = ps.executeUpdate();

            // If update was successful show success message
            if (rows > 0) {
                session.setAttribute("success", "Profile updated successfully.");
            } else {
                session.setAttribute("error", "Failed to update profile.");
            }

            // Redirect back to profile page
            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            session.setAttribute("error", "Something went wrong. Please try again.");
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}