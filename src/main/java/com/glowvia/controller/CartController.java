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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when user visits the cart page
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current user from the session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Create empty list to store cart items
        List<Map<String, Object>> cartItems = new ArrayList<>();

        try (Connection conn = DbConfig.getDbConnection()) {

            // Get the cart id for the current user
            int cartId = getCartId(conn, user.getId());

            // If cart not found show empty cart
            if (cartId == -1) {
                request.setAttribute("cartItems", cartItems);
                request.setAttribute("total_amount", 0.0);
                request.getRequestDispatcher("/pages/customer/cart.jsp").forward(request, response);
                return;
            }

            // Fetching all cart items for the user
            String sql = "SELECT p.id, p.name, p.description, p.photo_path, p.price, ci.quantity, ci.cart_item_id " +
                         "FROM cart_items ci " +
                         "JOIN products p ON ci.product_id = p.id " +
                         "WHERE ci.cart_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            // Loop through all cart items and add them to the list
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", rs.getInt("id"));
                item.put("name", rs.getString("name"));
                item.put("description", rs.getString("description"));
                item.put("photoPath", rs.getString("photo_path"));
                item.put("price", rs.getDouble("price"));
                item.put("quantity", rs.getInt("quantity"));
                item.put("cartItemId", rs.getInt("cart_item_id"));
                cartItems.add(item);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching cart items");
        }

        // Calculate the total amount of all items in the cart
        double totalAmount = 0.0;
        for (Map<String, Object> item : cartItems) {
            totalAmount += (double) item.get("price") * (int) item.get("quantity");
        }

        // Setting the cart items and total as session attributes
        session.setAttribute("cartItems", cartItems);
        request.setAttribute("total_amount", totalAmount);
        request.setAttribute("pageTitle", "Your Cart - Glowvia");

        // Forwarding to the cart JSP page
        request.getRequestDispatcher("/pages/customer/cart.jsp").forward(request, response);
    }

    // This method handles POST requests by calling doGet
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // This method gets the cart id for a user from the database
    private int getCartId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT cart_id FROM carts WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("cart_id");
        } else {
            return -1;
        }
    }
}