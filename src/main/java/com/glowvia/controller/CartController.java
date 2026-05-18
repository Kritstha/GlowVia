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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/cart", "/cart/add", "/cart/update", "/cart/remove"})
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
                request.setAttribute("pageTitle", "Your Cart - Glowvia");
                request.getRequestDispatcher("/pages/customer/cart.jsp").forward(request, response);
                return;
            }

            // Fetching all cart items for the user
            String sql = "SELECT p.id, p.name, p.description, p.photo_path, p.price, ci.quantity, ci.cart_item_id "
                       + "FROM cart_items ci "
                       + "JOIN products p ON ci.product_id = p.id "
                       + "WHERE ci.cart_id = ?";
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
            return;
        }

        // Calculate the total amount of all items in the cart
        double totalAmount = 0.0;
        for (Map<String, Object> item : cartItems) {
            totalAmount += (double) item.get("price") * (int) item.get("quantity");
        }

        // Setting the cart items and total as attributes
        session.setAttribute("cartItems", cartItems);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total_amount", totalAmount);
        request.setAttribute("pageTitle", "Your Cart - Glowvia");

        // Forwarding to the cart JSP page
        request.getRequestDispatcher("/pages/customer/cart.jsp").forward(request, response);
    }

    // This method handles POST requests for add update and remove
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current user from the session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get the requested path
        String path = request.getServletPath();

        try (Connection conn = DbConfig.getDbConnection()) {

            // Handle add to cart request
            if ("/cart/add".equals(path)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                // Get or create cart for this user
                int cartId = getOrCreateCart(conn, user.getId());

                // Check if product already exists in cart
                String checkSql = "SELECT cart_item_id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, cartId);
                checkStmt.setInt(2, productId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Product already in cart so update quantity
                    int newQty = rs.getInt("quantity") + quantity;
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, newQty);
                    updateStmt.setInt(2, rs.getInt("cart_item_id"));
                    updateStmt.executeUpdate();
                } else {
                    // Product not in cart so insert new item
                    String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setInt(1, cartId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }

                // Update cart count in session
                int cartCount = getCartCount(conn, cartId);
                session.setAttribute("cartCount", cartCount);
                session.setAttribute("success", "Item added to cart!");

                // Redirect back to the product page
                String referer = request.getHeader("Referer");
                if (referer != null) {
                    response.sendRedirect(referer);
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart");
                }
                return;
            }

            // Handle update cart item quantity
            if ("/cart/update".equals(path)) {
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                // Update the quantity in the database
                String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, cartItemId);
                updateStmt.executeUpdate();

                session.setAttribute("success", "Cart updated.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Handle remove cart item
            if ("/cart/remove".equals(path)) {
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));

                // Delete the item from the database
                String deleteSql = "DELETE FROM cart_items WHERE cart_item_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, cartItemId);
                deleteStmt.executeUpdate();

                session.setAttribute("success", "Item removed from cart.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            session.setAttribute("error", "Something went wrong. Please try again.");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    // This method gets the cart id for a user from the database
    private int getCartId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT cart_id FROM carts WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("cart_id");
        }
        return -1;
    }

    // This method gets or creates a cart for a user
    private int getOrCreateCart(Connection conn, int userId) throws SQLException {
        // First check if cart exists
        int cartId = getCartId(conn, userId);

        // If cart does not exist create a new one
        if (cartId == -1) {
            String insertSql = "INSERT INTO carts (user_id) VALUES (?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, userId);
            insertStmt.executeUpdate();
            ResultSet keys = insertStmt.getGeneratedKeys();
            if (keys.next()) {
                cartId = keys.getInt(1);
            }
        }
        return cartId;
    }

    // This method gets the total number of items in the cart
    private int getCartCount(Connection conn, int cartId) throws SQLException {
        String sql = "SELECT SUM(quantity) FROM cart_items WHERE cart_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, cartId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}