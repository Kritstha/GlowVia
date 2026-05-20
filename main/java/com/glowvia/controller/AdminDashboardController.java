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

/*
  This controller handles the admin dashboard page
  It is mapped to the /admin/dashboard URL
  It shows the total number of users, orders and products as KPI cards
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
     These are the service classes used to get data from the database
      DashboardService gets all users
      OrderService gets all orders
      ProductService gets all products
     */
    private DashboardService dashboardService = new DashboardService();
    private OrderService orderService = new OrderService();
    private ProductService productService = new ProductService();

    /*
     This method runs when the admin visits the dashboard page
      It gets all the KPI data and forwards to the dashboard JSP page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Get all users from the database and set total count as attribute
        List<User> users = dashboardService.getAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("totalUsers", users.size());

       
        int totalOrders = orderService.getAllOrders().size();
        request.setAttribute("totalOrders", totalOrders);


        int totalProducts = productService.getAllProducts().size();
        request.setAttribute("totalProducts", totalProducts);

        request.setAttribute("pageTitle", "Admin Dashboard - Glowvia");

        
        request.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(request, response);
    }
}