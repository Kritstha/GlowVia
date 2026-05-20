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

/*
 This controller handles the checkout process for the customer
 When customer clicks Place Order it inserts the order into the database
 clears the cart and redirects to the orders page
*/
@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        com.glowvia.model.User user = (com.glowvia.model.User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        double totalAmount = 0;

        try (Connection conn = DbConfig.getDbConnection()) {

            /*
             This block checks stock for each product before placing the order
             If any product has less stock than ordered it shows an error
            */
            for (Map<String, Object> cartItem : cartItems) {
                int productId = (int) cartItem.get("productId");
                int quantity = (int) cartItem.get("quantity");

                String stockSql = "SELECT stock_quantity, name FROM products WHERE id = ?";
                PreparedStatement stockCheckStmt = conn.prepareStatement(stockSql);
                stockCheckStmt.setInt(1, productId);
                ResultSet stockRs = stockCheckStmt.executeQuery();

                if (stockRs.next()) {
                    int currentStock = stockRs.getInt("stock_quantity");
                    String productName = stockRs.getString("name");
                    if (currentStock < quantity) {
                        session.setAttribute("error", "Not enough stock for " + productName + ". Only " + currentStock + " left.");
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return;
                    }
                }
            }

            /*
             This inserts a new order into the orders table
             Total amount is set to 0 first and updated after all items are calculated
            */
            String insertOrderSql = "INSERT INTO orders (user_id, total_amount, status, order_date) VALUES (?, ?, ?, ?)";
            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, user.getId());
            orderStmt.setDouble(2, totalAmount);
            orderStmt.setString(3, "Pending");
            orderStmt.setString(4, new java.sql.Date(System.currentTimeMillis()).toString());

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows > 0) {

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    /*
                     This inserts each cart item as an order item
                     It stores product id, price at time of purchase and quantity
                    */
                    String insertOrderItemSql = "INSERT INTO order_items (order_id, product_id, price_at_purchase, quantity) VALUES (?, ?, ?, ?)";
                    PreparedStatement itemStmt = conn.prepareStatement(insertOrderItemSql);

                    for (Map<String, Object> cartItem : cartItems) {
                        itemStmt.setInt(1, orderId);
                        itemStmt.setInt(2, (int) cartItem.get("productId"));
                        itemStmt.setDouble(3, (double) cartItem.get("price"));
                        itemStmt.setInt(4, (int) cartItem.get("quantity"));
                        itemStmt.addBatch();
                        totalAmount += (double) cartItem.get("price") * (int) cartItem.get("quantity");
                    }

                    itemStmt.executeBatch();

                    /*
                     This reduces the stock for each product that was ordered
                     It subtracts the ordered quantity from current stock
                    */
                    String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";
                    PreparedStatement stockStmt = conn.prepareStatement(updateStockSql);

                    for (Map<String, Object> cartItem : cartItems) {
                        stockStmt.setInt(1, (int) cartItem.get("quantity"));
                        stockStmt.setInt(2, (int) cartItem.get("productId"));
                        stockStmt.addBatch();
                    }

                    stockStmt.executeBatch();

                    String updateOrderSql = "UPDATE orders SET total_amount = ? WHERE order_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateOrderSql);
                    updateStmt.setDouble(1, totalAmount);
                    updateStmt.setInt(2, orderId);
                    updateStmt.executeUpdate();


                    String clearCartSql = "DELETE FROM cart_items WHERE cart_id = (SELECT cart_id FROM carts WHERE user_id = ?)";
                    PreparedStatement clearStmt = conn.prepareStatement(clearCartSql);
                    clearStmt.setInt(1, user.getId());
                    clearStmt.executeUpdate();
                }
            }


            session.removeAttribute("cartItems");
            session.setAttribute("cartCount", 0);
            session.setAttribute("success", "Order placed successfully!");
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error placing the order");
        }
    }
}