package com.glowvia.controller;

import com.glowvia.model.Order;
import com.glowvia.model.User;
import com.glowvia.service.OrderService;
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
import java.sql.SQLException;
import java.util.List;

/*
  This controller handles the admin orders page
  It is mapped to the /admin/orders URL
  Admin can view all orders and update the status of each order
 */
@WebServlet("/admin/orders")
public class AdminOrderController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
      This is the order service class
      It handles all database operations related to orders
     */
    private OrderService orderService = new OrderService();

    /*
     This method runs when the admin visits the orders page
     It gets all orders from the database and forwards to the orders JSP page
     */
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

        // Get all orders from the database using the order service
        List<Order> orders = orderService.getAllOrders();

        // Print the number of orders fetched for debugging
        System.out.println("Orders fetched in controller: " + orders.size());

        // Set the orders list and page title as request attributes
        request.setAttribute("orders", orders);
        request.setAttribute("pageTitle", "Manage Orders - Glowvia");

        // Forward to the admin orders JSP page to display the orders
        request.getRequestDispatcher("/pages/admin/orders.jsp").forward(request, response);
    }

    /*
      This method runs when admin updates the status of an order
      It gets the order id and new status from the form and updates the database
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the order id and new status from the form
        String orderIdParam = request.getParameter("orderId");
        String status = request.getParameter("status");

        // Check if order id is valid and show error if it is not
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            request.getSession().setAttribute("error", "Invalid order id.");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try (Connection conn = DbConfig.getDbConnection()) {

            // Update the order status in the database using the order id
            String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, Integer.parseInt(orderIdParam));

            int rows = stmt.executeUpdate();

            // Check if update was successful and set the flash message
            if (rows > 0) {
                request.getSession().setAttribute("success", "Order status updated successfully.");
            } else {
                request.getSession().setAttribute("error", "Failed to update order status.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Something went wrong. Please try again.");
        }

        // Redirect back to the admin orders page after update
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}