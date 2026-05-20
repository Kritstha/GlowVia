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

        List<Order> orders = orderService.getAllOrders();
        System.out.println("Orders fetched in controller: " + orders.size());
        request.setAttribute("orders", orders);
        request.setAttribute("pageTitle", "Manage Orders - Glowvia");
        request.getRequestDispatcher("/pages/admin/orders.jsp").forward(request, response);
    }

    /*
      This method runs when admin updates the status of an order
      It gets the order id and new status from the form and updates the database
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String orderIdParam = request.getParameter("orderId");
        String status = request.getParameter("status");
        
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            request.getSession().setAttribute("error", "Invalid order id.");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try (Connection conn = DbConfig.getDbConnection()) {

 
            String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, Integer.parseInt(orderIdParam));

            int rows = stmt.executeUpdate();


            if (rows > 0) {
                request.getSession().setAttribute("success", "Order status updated successfully.");
            } else {
                request.getSession().setAttribute("error", "Failed to update order status.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Something went wrong. Please try again.");
        }


        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}