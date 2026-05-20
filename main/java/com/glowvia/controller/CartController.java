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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Map<String, Object>> cartItems = new ArrayList<>();

        try (Connection conn = DbConfig.getDbConnection()) {

            int cartId = getCartId(conn, user.getId());

            if (cartId == -1) {
                request.setAttribute("cartItems", cartItems);
                request.setAttribute("total_amount", 0.0);
                request.setAttribute("pageTitle", "Your Cart - Glowvia");
                request.getRequestDispatcher("/pages/customer/cart.jsp").forward(request, response);
                return;
            }

            // Remove out of stock items from cart automatically
            String removeOutOfStockSql = "DELETE FROM cart_items WHERE cart_id = ? AND product_id IN (SELECT id FROM products WHERE stock_quantity <= 0)";
            PreparedStatement removeStmt = conn.prepareStatement(removeOutOfStockSql);
            removeStmt.setInt(1, cartId);
            int removedItems = removeStmt.executeUpdate();

            if (removedItems > 0) {
                session.setAttribute("error", "Some items were removed because they are out of stock.");
            }

            String sql = "SELECT p.id, p.name, p.description, p.photo_path, p.price, p.stock_quantity, ci.quantity, ci.cart_item_id "
                    + "FROM cart_items ci JOIN products p ON ci.product_id = p.id WHERE ci.cart_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", rs.getInt("id"));
                item.put("name", rs.getString("name"));
                item.put("description", rs.getString("description"));
                item.put("photoPath", rs.getString("photo_path"));
                item.put("price", rs.getDouble("price"));
                item.put("quantity", rs.getInt("quantity"));
                item.put("cartItemId", rs.getInt("cart_item_id"));
                item.put("stockQuantity", rs.getInt("stock_quantity"));
                cartItems.add(item);
            }

            int totalCount = 0;
            for (Map<String, Object> item : cartItems) {
                totalCount += (int) item.get("quantity");
            }
            session.setAttribute("cartCount", totalCount);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching cart items");
            return;
        }

        double totalAmount = 0.0;
        for (Map<String, Object> item : cartItems) {
            totalAmount += (double) item.get("price") * (int) item.get("quantity");
        }

        session.setAttribute("cartItems", cartItems);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total_amount", totalAmount);
        request.setAttribute("pageTitle", "Your Cart - Glowvia");
        request.getRequestDispatcher("/pages/customer/cart.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();

        try (Connection conn = DbConfig.getDbConnection()) {

            if ("/cart/add".equals(path)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                String stockSql = "SELECT stock_quantity, name FROM products WHERE id = ?";
                PreparedStatement stockStmt = conn.prepareStatement(stockSql);
                stockStmt.setInt(1, productId);
                ResultSet stockRs = stockStmt.executeQuery();

                if (stockRs.next()) {
                    int currentStock = stockRs.getInt("stock_quantity");
                    String productName = stockRs.getString("name");
                    if (currentStock < quantity) {
                        session.setAttribute("error", "Not enough stock for " + productName + ". Only " + currentStock + " left.");
                        String referer = request.getHeader("Referer");
                        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/cart");
                        return;
                    }
                }

                int cartId = getOrCreateCart(conn, user.getId());

                String checkSql = "SELECT cart_item_id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, cartId);
                checkStmt.setInt(2, productId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int newQty = rs.getInt("quantity") + quantity;
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, newQty);
                    updateStmt.setInt(2, rs.getInt("cart_item_id"));
                    updateStmt.executeUpdate();
                } else {
                    String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setInt(1, cartId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }

                int cartCount = getCartCount(conn, cartId);
                session.setAttribute("cartCount", cartCount);
                session.setAttribute("success", "Item added to cart!");

                String referer = request.getHeader("Referer");
                response.sendRedirect(referer != null ? referer : request.getContextPath() + "/cart");
                return;
            }

            if ("/cart/update".equals(path)) {
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                String getProductSql = "SELECT product_id FROM cart_items WHERE cart_item_id = ?";
                PreparedStatement getProductStmt = conn.prepareStatement(getProductSql);
                getProductStmt.setInt(1, cartItemId);
                ResultSet productRs = getProductStmt.executeQuery();

                if (productRs.next()) {
                    int productId = productRs.getInt("product_id");
                    String stockSql = "SELECT stock_quantity FROM products WHERE id = ?";
                    PreparedStatement stockStmt = conn.prepareStatement(stockSql);
                    stockStmt.setInt(1, productId);
                    ResultSet stockRs = stockStmt.executeQuery();

                    if (stockRs.next()) {
                        int currentStock = stockRs.getInt("stock_quantity");
                        if (quantity > currentStock) {
                            session.setAttribute("error", "Only " + currentStock + " units available in stock.");
                            response.sendRedirect(request.getContextPath() + "/cart");
                            return;
                        }
                    }
                }

                String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, cartItemId);
                updateStmt.executeUpdate();

                session.setAttribute("success", "Cart updated.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            if ("/cart/remove".equals(path)) {
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
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

    private int getCartId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT cart_id FROM carts WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt("cart_id");
        return -1;
    }

    private int getOrCreateCart(Connection conn, int userId) throws SQLException {
        int cartId = getCartId(conn, userId);
        if (cartId == -1) {
            String insertSql = "INSERT INTO carts (user_id) VALUES (?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, userId);
            insertStmt.executeUpdate();
            ResultSet keys = insertStmt.getGeneratedKeys();
            if (keys.next()) cartId = keys.getInt(1);
        }
        return cartId;
    }

    private int getCartCount(Connection conn, int cartId) throws SQLException {
        String sql = "SELECT SUM(quantity) FROM cart_items WHERE cart_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, cartId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }
}