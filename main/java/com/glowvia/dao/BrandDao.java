package com.glowvia.dao;

import com.glowvia.model.Brand;
import com.glowvia.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access layer for `brands` table.
 */
public class BrandDao {

    private Brand map(ResultSet rs) throws SQLException {
        Brand b = new Brand();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("name"));
        b.setContact(rs.getString("contact"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        b.setUpdatedAt(rs.getTimestamp("updated_at"));
        return b;
    }

    public List<Brand> listAll() {
        List<Brand> out = new ArrayList<>();
        String sql = "SELECT id, name, contact, created_at, updated_at "
                   + "FROM brands ORDER BY name ASC";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public Brand findById(int id) {
        String sql = "SELECT id, name, contact, created_at, updated_at "
                   + "FROM brands WHERE id = ?";
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

    public boolean nameExists(String name) {
        String sql = "SELECT 1 FROM brands WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public boolean insert(Brand b) {
        String sql = "INSERT INTO brands (name, contact) VALUES (?, ?)";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getName());
            ps.setString(2, b.getContact());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM brands WHERE id = ?";
        try (Connection c = DbConnection.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
