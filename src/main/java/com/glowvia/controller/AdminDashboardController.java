package com.glowvia.controller;

import com.glowvia.model.User;
import com.glowvia.service.DashboardService;
import com.glowvia.service.OrderService;
import com.glowvia.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Service classes for getting data from database
    private DashboardService dashboardService = new DashboardService();
    private OrderService orderService = new OrderService();
    private ProductService productService = new ProductService();

    // This method runs when admin visits the dashboard page
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

        // Get all users from the database
        List<User> users = dashboardService.getAllUsers();

        // Set total users count as attribute
        request.setAttribute("users", users);
        request.setAttribute("totalUsers", users.size());

        // Get total orders count and set as attribute
        int totalOrders = orderService.getAllOrders().size();
        request.setAttribute("totalOrders", totalOrders);

        // Get total products count and set as attribute
        int totalProducts = productService.getAllProducts().size();
        request.setAttribute("totalProducts", totalProducts);

        request.setAttribute("pageTitle", "Admin Dashboard - Glowvia");

        // Forward to the admin dashboard page
        request.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(request, response);
    }
}