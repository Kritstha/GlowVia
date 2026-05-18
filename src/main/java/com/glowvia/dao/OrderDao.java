package com.glowvia.dao;

import com.glowvia.model.CartItem;
import com.glowvia.model.Order;
import com.glowvia.model.OrderItem;
import com.glowvia.util.DbConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for orders. Order placement is wrapped in a JDBC transaction
 * so stock decrement, order creation and cart clear succeed together or roll
 * back as a unit.
 */
public class OrderDao {

    private final ProductDao productDao = new ProductDao();
    private final CartDao cartDao = new CartDao();

    /**
     * Places an order using the items currently in the user's cart.
     * Returns the new order id, or -1 if the transaction failed.
     */
    public int placeOrder(int userId, int cartId, List<CartItem> items,
                          BigDecimal total) {

        if (items == null || items.isEmpty()) return -1;

        Connection c = null;
        try {
            c = DbConnection.open();
            c.setAutoCommit(false);

            // 1) insert order header
            int orderId;
            String insOrder = "INSERT INTO orders (user_id, total_amount, status) "
                            + "VALUES (?, ?, 'Pending')";
            try (PreparedStatement ps = c.prepareStatement(insOrder,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setBigDecimal(2, total);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Order id not generated");
                    orderId = keys.getInt(1);
                }
            }

            // 2) insert each line and decrement stock
            String insItem = "INSERT INTO order_items "
                    + "(order_id, product_id, quantity, price_at_purchase) "
                    + "VALUES (?, ?, ?, ?)";
            for (CartItem ci : items) {
                if (ci.getQuantity() > ci.getStockQuantity()) {
                    throw new SQLException("Insufficient stock for "
                            + ci.getProductName());
                }
                try (PreparedStatement ps = c.prepareStatement(insItem)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, ci.getProductId());
                    ps.setInt(3, ci.getQuantity());
                    ps.setBigDecimal(4, BigDecimal.valueOf(ci.getPrice()));
                    ps.executeUpdate();
                }
                if (!productDao.decreaseStock(c, ci.getProductId(), ci.getQuantity())) {
                    throw new SQLException("Stock update failed for product "
                            + ci.getProductId());
                }
            }

            // 3) clear the cart
            cartDao.clearCart(c, cartId);

            c.commit();
            return orderId;
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (c != null) {
                try { c.rollback(); } catch (SQLException ignore) { /* */ }
            }
            return -1;
        } finally {
            if (c != null) {
                try { c.setAutoCommit(true); c.close(); }
                catch (SQLException ignore) { /* */ }
            }
        }
    }

    public List<Order> listForUser(int userId) {
        String sql = "SELECT order_id, user_id, order_date, total_amount, status "
                   + "FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        List<Order> out = new ArrayList<>();
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public List<Order> listAll() {
        String sql = "SELECT o.order_id, o.user_id, o.order_date, o.total_amount, "
                   + "o.status, u.full_name "
                   + "FROM orders o JOIN users u ON u.id = o.user_id "
                   + "ORDER BY o.order_date DESC";
        List<Order> out = new ArrayList<>();
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapOrder(rs);
                o.setCustomerName(rs.getString("full_name"));
                out.add(o);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public Order findById(int orderId) {
        String sql = "SELECT o.order_id, o.user_id, o.order_date, o.total_amount, "
                   + "o.status, u.full_name "
                   + "FROM orders o JOIN users u ON u.id = o.user_id "
                   + "WHERE o.order_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = mapOrder(rs);
                    o.setCustomerName(rs.getString("full_name"));
                    o.setItems(listItems(orderId));
                    return o;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<OrderItem> listItems(int orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, "
                   + "oi.quantity, oi.price_at_purchase, p.name, p.photo_path "
                   + "FROM order_items oi JOIN products p ON p.id = oi.product_id "
                   + "WHERE oi.order_id = ?";
        List<OrderItem> out = new ArrayList<>();
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setOrderItemId(rs.getInt("order_item_id"));
                    oi.setOrderId(rs.getInt("order_id"));
                    oi.setProductId(rs.getInt("product_id"));
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    oi.setProductName(rs.getString("name"));
                    oi.setPhotoPath(rs.getString("photo_path"));
                    out.add(oi);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int countTotal() { return countSingle("SELECT COUNT(*) FROM orders"); }
    public int countPending() {
        return countSingle("SELECT COUNT(*) FROM orders WHERE status = 'Pending'");
    }

    private int countSingle(String sql) {
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public BigDecimal totalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders "
                   + "WHERE status <> 'Cancelled'";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setOrderDate(rs.getTimestamp("order_date"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        return o;
    }
}
