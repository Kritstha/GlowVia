package com.glowvia.controller;

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
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    // This method runs when user submits the checkout form
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current session
        HttpSession session = request.getSession();

        // Retrieve the cart items from the session
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");

        // If cart is empty redirect back to cart page
        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Get the current user from the session
        com.glowvia.model.User user = (com.glowvia.model.User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Variable to store the total amount of the order
        double totalAmount = 0;

        try (Connection conn = DbConfig.getDbConnection()) {

            // Insert the order into the orders table
            String insertOrderSql = "INSERT INTO orders (user_id, total_amount, status, order_date) VALUES (?, ?, ?, ?)";
            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, user.getId());
            orderStmt.setDouble(2, totalAmount);
            orderStmt.setString(3, "Pending");
            orderStmt.setString(4, new java.sql.Date(System.currentTimeMillis()).toString());

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows > 0) {
                // Get the generated order id
                ResultSet generatedKeys = orderStmt.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // Insert each cart item into the order_items table
                    String insertOrderItemSql = "INSERT INTO order_items (order_id, product_id, price_at_purchase, quantity) VALUES (?, ?, ?, ?)";
                    PreparedStatement itemStmt = conn.prepareStatement(insertOrderItemSql);

                    // Loop through all cart items and add them to order
                    for (Map<String, Object> cartItem : cartItems) {
                        itemStmt.setInt(1, orderId);
                        itemStmt.setInt(2, (int) cartItem.get("productId"));
                        itemStmt.setDouble(3, (double) cartItem.get("price"));
                        itemStmt.setInt(4, (int) cartItem.get("quantity"));
                        itemStmt.addBatch();
                        // Add item total to overall total
                        totalAmount += (double) cartItem.get("price") * (int) cartItem.get("quantity");
                    }

                    // Execute all order item insertions at once
                    itemStmt.executeBatch();

                    // Update the stock quantity for each product ordered
                    String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";
                    PreparedStatement stockStmt = conn.prepareStatement(updateStockSql);

                    for (Map<String, Object> cartItem : cartItems) {
                        stockStmt.setInt(1, (int) cartItem.get("quantity"));
                        stockStmt.setInt(2, (int) cartItem.get("productId"));
                        stockStmt.addBatch();
                    }
                    // Execute all stock updates at once
                    stockStmt.executeBatch();

                    // Update the total amount in the orders table
                    String updateOrderSql = "UPDATE orders SET total_amount = ? WHERE order_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateOrderSql);
                    updateStmt.setDouble(1, totalAmount);
                    updateStmt.setInt(2, orderId);
                    updateStmt.executeUpdate();

                    // Clear the cart items from the carts table
                    String clearCartSql = "DELETE FROM cart_items WHERE cart_id = (SELECT cart_id FROM carts WHERE user_id = ?)";
                    PreparedStatement clearStmt = conn.prepareStatement(clearCartSql);
                    clearStmt.setInt(1, user.getId());
                    clearStmt.executeUpdate();
                }
            }

            // Remove cart items from the session after order is placed
            session.removeAttribute("cartItems");
            session.setAttribute("cartCount", 0);

            // Redirect to order success page
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error placing the order");
        }
    }
}