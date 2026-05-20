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
import java.util.List;

/*
  This controller handles the admin users page
  It is mapped to the /admin/users URL
  Admin can view all registered customers in the database
 */
@WebServlet("/admin/users")
public class AdminUserController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
      This method runs when the admin visits the users page
      It gets all customers from the database and forwards to the users JSP page
      Admin accounts are excluded from the list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current user from the session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

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

            /*
              This SQL query gets all customers from the database
              It only gets users with the role of user and excludes admin accounts
              Results are ordered by newest first
             */
            String sql = "SELECT * FROM users WHERE user_role = 'user' ORDER BY id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("user_role"));
                users.add(user);
            }
            
            System.out.println("Users fetched in controller: " + users.size());

            request.setAttribute("users", users);
            request.setAttribute("pageTitle", "Manage Users - Glowvia");
            
            request.getRequestDispatcher("/pages/admin/users.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }
}