package com.glowvia.controller;

import com.glowvia.model.Order;
import com.glowvia.model.User;
import com.glowvia.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Service class that handles database operations for orders
    private OrderService orderService = new OrderService();

    // This method runs when user visits the orders page
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

        // Fetch all orders for the current user from the service
        List<Order> orders = orderService.getOrdersByUserId(currentUser.getId());

        // Set the orders as a request attribute
        request.setAttribute("orders", orders);
        request.setAttribute("pageTitle", "My Orders - Glowvia");

        // Forward the request to orders.jsp
        request.getRequestDispatcher("/pages/customer/orders.jsp").forward(request, response);
    }
}