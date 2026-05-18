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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/orders", "/orders/view"})
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

        // Get the requested path
        String path = request.getServletPath();

        // Handle order detail view
        if ("/orders/view".equals(path)) {
            String orderIdParam = request.getParameter("id");

            // If no order id redirect to orders page
            if (orderIdParam == null || orderIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            try {
                int orderId = Integer.parseInt(orderIdParam);

                try (Connection conn = DbConfig.getDbConnection()) {

                    // Get the order details from the database
                    String orderSql = "SELECT * FROM orders WHERE order_id = ? AND user_id = ?";
                    PreparedStatement orderStmt = conn.prepareStatement(orderSql);
                    orderStmt.setInt(1, orderId);
                    orderStmt.setInt(2, currentUser.getId());
                    ResultSet orderRs = orderStmt.executeQuery();

                    // If order not found redirect to orders page
                    if (!orderRs.next()) {
                        response.sendRedirect(request.getContextPath() + "/orders");
                        return;
                    }

                    // Create order object with details
                    Order order = new Order();
                    order.setOrderId(orderRs.getInt("order_id"));
                    order.setUserId(orderRs.getInt("user_id"));
                    order.setTotalAmount(orderRs.getBigDecimal("total_amount"));
                    order.setStatus(orderRs.getString("status"));
                    order.setOrderDate(orderRs.getTimestamp("order_date"));

                    // Get all order items for this order
                    String itemsSql = "SELECT oi.*, p.name as product_name, p.photo_path "
                                    + "FROM order_items oi "
                                    + "JOIN products p ON oi.product_id = p.id "
                                    + "WHERE oi.order_id = ?";
                    PreparedStatement itemsStmt = conn.prepareStatement(itemsSql);
                    itemsStmt.setInt(1, orderId);
                    ResultSet itemsRs = itemsStmt.executeQuery();

                    // Loop through all order items and add them to list
                    List<Map<String, Object>> items = new ArrayList<>();
                    while (itemsRs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("productName", itemsRs.getString("product_name"));
                        item.put("photoPath", itemsRs.getString("photo_path"));
                        item.put("quantity", itemsRs.getInt("quantity"));
                        item.put("priceAtPurchase", itemsRs.getBigDecimal("price_at_purchase"));
                        item.put("lineTotal", itemsRs.getBigDecimal("price_at_purchase")
                                .multiply(new java.math.BigDecimal(itemsRs.getInt("quantity"))));
                        items.add(item);
                    }

                    // Pass order and items to the page
                    request.setAttribute("order", order);
                    request.setAttribute("orderItems", items);
                    request.setAttribute("pageTitle", "Order #" + orderId + " - Glowvia");

                    // Forward to order view page
                    request.getRequestDispatcher("/pages/customer/order_view.jsp").forward(request, response);

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/orders");
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/orders");
            }
            return;
        }

        // Default: show all orders for current user
        List<Order> orders = orderService.getOrdersByUserId(currentUser.getId());

        // Set the orders as a request attribute
        request.setAttribute("orders", orders);
        request.setAttribute("pageTitle", "My Orders - Glowvia");

        // Forward the request to orders.jsp
        request.getRequestDispatcher("/pages/customer/orders.jsp").forward(request, response);
    }
}