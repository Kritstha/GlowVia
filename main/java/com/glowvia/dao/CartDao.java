package com.glowvia.dao;

import com.glowvia.model.CartItem;
import com.glowvia.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for shopping cart - covers cart creation, items, totals.
 */
public class CartDao {

    /** Return the cart_id for the given user, creating one if none exists. */
    public int ensureCart(int userId) {
        String find = "SELECT cart_id FROM carts WHERE user_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(find)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cart_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }

        String ins = "INSERT INTO carts (user_id) VALUES (?)";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public List<CartItem> listItems(int cartId) {
        String sql = "SELECT ci.cart_item_id, ci.cart_id, ci.product_id, ci.quantity, "
                   + "p.name, p.price, p.photo_path, p.stock_quantity, b.name AS brand_name "
                   + "FROM cart_items ci "
                   + "JOIN products p ON p.id = ci.product_id "
                   + "LEFT JOIN brands b ON b.id = p.brand_id "
                   + "WHERE ci.cart_id = ? "
                   + "ORDER BY ci.cart_item_id ASC";

        List<CartItem> out = new ArrayList<>();
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem();
                    ci.setCartItemId(rs.getInt("cart_item_id"));
                    ci.setCartId(rs.getInt("cart_id"));
                    ci.setProductId(rs.getInt("product_id"));
                    ci.setQuantity(rs.getInt("quantity"));
                    ci.setProductName(rs.getString("name"));
                    ci.setPrice(rs.getDouble("price"));
                    ci.setPhotoPath(rs.getString("photo_path"));
                    ci.setStockQuantity(rs.getInt("stock_quantity"));
                    ci.setBrandName(rs.getString("brand_name"));
                    out.add(ci);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public int countItems(int userId) {
        String sql = "SELECT COALESCE(SUM(ci.quantity), 0) "
                   + "FROM cart_items ci JOIN carts c ON c.cart_id = ci.cart_id "
                   + "WHERE c.user_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean addOrIncrement(int cartId, int productId, int qty) {
        // Check if line already exists
        String find = "SELECT cart_item_id, quantity FROM cart_items "
                    + "WHERE cart_id = ? AND product_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(find)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("cart_item_id");
                    int newQty = rs.getInt("quantity") + qty;
                    return updateQuantity(id, newQty);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        // Otherwise insert
        String ins = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(ins)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateQuantity(int cartItemId, int qty) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, cartItemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean removeItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(Connection c, int cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
            return true;
        }
    }
}
