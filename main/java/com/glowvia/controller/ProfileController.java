package com.glowvia.controller;

import com.glowvia.model.User;
import com.glowvia.util.DbConfig;
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
 This controller handles the customer profile page
 Customers can view profile, update details and change password
*/
@WebServlet({"/profile", "/profile/password"})
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
     This method runs when customer visits the profile page
     It gets the latest user details from database and shows the profile page
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try (Connection conn = DbConfig.getDbConnection()) {

            // Get latest user details from database
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();

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

                request.setAttribute("user", user);
                request.setAttribute("pageTitle", "My Profile - Glowvia");
                request.getRequestDispatcher("/pages/customer/profile.jsp").forward(request, response);

            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    /*
     This method handles both profile update and password update
     It checks the path to decide which action to perform
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();

        // Handle password update request
        if ("/profile/password".equals(path)) {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            // Check if new password and confirm password match
            if (!newPassword.equals(confirmPassword)) {
                session.setAttribute("error", "New passwords do not match.");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            // Check if new password is at least 6 characters
            if (newPassword.length() < 6) {
                session.setAttribute("error", "New password must be at least 6 characters.");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            try (Connection conn = DbConfig.getDbConnection()) {

                // Get current hashed password from database
                String getSql = "SELECT password FROM users WHERE id = ?";
                PreparedStatement getPs = conn.prepareStatement(getSql);
                getPs.setInt(1, currentUser.getId());
                ResultSet rs = getPs.executeQuery();

                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    // Check if current password entered is correct
                    if (!BCrypt.checkpw(currentPassword, hashedPassword)) {
                        session.setAttribute("error", "Current password is incorrect.");
                        response.sendRedirect(request.getContextPath() + "/profile");
                        return;
                    }

                    // Hash the new password and update in database
                    String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
                    String updateSql = "UPDATE users SET password = ? WHERE id = ?";
                    PreparedStatement updatePs = conn.prepareStatement(updateSql);
                    updatePs.setString(1, newHashedPassword);
                    updatePs.setInt(2, currentUser.getId());

                    int rows = updatePs.executeUpdate();

                    if (rows > 0) {
                        session.setAttribute("success", "Password updated successfully.");
                    } else {
                        session.setAttribute("error", "Failed to update password.");
                    }
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                session.setAttribute("error", "Something went wrong. Please try again.");
            }

            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        // Handle profile update request
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");

        try (Connection conn = DbConfig.getDbConnection()) {

            // Update user details in database
            String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, dob = ?, gender = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, dob);
            ps.setString(5, gender);
            ps.setInt(6, currentUser.getId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                session.setAttribute("success", "Profile updated successfully.");
            } else {
                session.setAttribute("error", "Failed to update profile.");
            }

            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            session.setAttribute("error", "Something went wrong. Please try again.");
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}