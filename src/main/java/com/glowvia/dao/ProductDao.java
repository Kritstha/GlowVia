package com.glowvia.dao;

import com.glowvia.model.Product;
import com.glowvia.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for the `products` table, joining `brands` for the brand label.
 */
public class ProductDao {

    private static final String BASE_QUERY =
            "SELECT p.id, p.name, p.brand_id, p.category, p.skin_type, "
          + "p.key_ingredients, p.price, p.stock_quantity, p.description, "
          + "p.photo_path, b.name AS brand_name "
          + "FROM products p LEFT JOIN brands b ON p.brand_id = b.id ";

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setBrandId(rs.getInt("brand_id"));
        p.setBrandName(rs.getString("brand_name"));
        p.setCategory(rs.getString("category"));
        p.setSkinType(rs.getString("skin_type"));
        p.setKeyIngredients(rs.getString("key_ingredients"));
        p.setPrice(rs.getDouble("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setDescription(rs.getString("description"));
        p.setPhotoPath(rs.getString("photo_path"));
        return p;
    }

    public List<Product> listAll() {
        List<Product> out = new ArrayList<>();
        String sql = BASE_QUERY + "ORDER BY p.id DESC";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public List<Product> search(String keyword, String category) {
        StringBuilder sb = new StringBuilder(BASE_QUERY).append("WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sb.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? "
                    + "OR LOWER(p.key_ingredients) LIKE ? OR LOWER(b.name) LIKE ?) ");
            String pat = "%" + keyword.toLowerCase() + "%";
            params.add(pat); params.add(pat); params.add(pat); params.add(pat);
        }
        if (category != null && !category.isBlank()
                && !"all".equalsIgnoreCase(category)) {
            sb.append(" AND LOWER(p.category) = ? ");
            params.add(category.toLowerCase());
        }
        sb.append("ORDER BY p.id DESC");

        List<Product> out = new ArrayList<>();
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public Product findById(int id) {
        String sql = BASE_QUERY + "WHERE p.id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<String> distinctCategories() {
        List<String> out = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM products "
                   + "WHERE category IS NOT NULL AND category <> '' "
                   + "ORDER BY category ASC";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString(1));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public boolean insert(Product p) {
        String sql = "INSERT INTO products "
                + "(name, brand_id, category, skin_type, key_ingredients, "
                + " price, stock_quantity, description, photo_path) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bindCommon(ps, p);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Product p) {
        String sql = "UPDATE products SET name = ?, brand_id = ?, category = ?, "
                   + "skin_type = ?, key_ingredients = ?, price = ?, "
                   + "stock_quantity = ?, description = ?, photo_path = ? "
                   + "WHERE id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bindCommon(ps, p);
            ps.setInt(10, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void bindCommon(PreparedStatement ps, Product p) throws SQLException {
        ps.setString(1, p.getName());
        if (p.getBrandId() > 0) ps.setInt(2, p.getBrandId());
        else ps.setNull(2, java.sql.Types.INTEGER);
        ps.setString(3, p.getCategory());
        ps.setString(4, p.getSkinType());
        ps.setString(5, p.getKeyIngredients());
        ps.setDouble(6, p.getPrice());
        ps.setInt(7, p.getStockQuantity());
        ps.setString(8, p.getDescription());
        ps.setString(9, p.getPhotoPath());
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean decreaseStock(Connection c, int productId, int qty)
            throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? "
                   + "WHERE id = ? AND stock_quantity >= ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        }
    }
}
