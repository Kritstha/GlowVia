package com.glowvia.service;

import com.glowvia.model.Product;
import com.glowvia.util.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // This method gets a single product by its id from the database
    public Product getProductById(int productId) {
        String query = "SELECT p.*, b.name as brand_name FROM products p "
                     + "LEFT JOIN brands b ON p.brand_id = b.id "
                     + "WHERE p.id = ?";

        try (Connection conn = DbConfig.getDbConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            // If product found map it to Product object and return it
            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setCategory(rs.getString("category"));
                product.setSkinType(rs.getString("skin_type"));
                product.setKeyIngredients(rs.getString("key_ingredients"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setDescription(rs.getString("description"));
                product.setPhotoPath(rs.getString("photo_path"));
                System.out.println(productId);
                System.out.println(product);
                return product;
            }
            return null;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches for products based on a keyword in their name, brand or ingredients.
     *
     * @param keyword The search keyword to match against product name, brand and ingredients.
     * @return A list of Product objects that match the keyword.
     */
    public List<Product> searchProducts(String keyword) {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT p.*, b.name as brand_name FROM products p "
                     + "LEFT JOIN brands b ON p.brand_id = b.id "
                     + "WHERE p.name LIKE ? "
                     + "OR b.name LIKE ? "
                     + "OR p.key_ingredients LIKE ?";

        try (Connection conn = DbConfig.getDbConnection()) {
            // Prepare the SQL statement
            PreparedStatement ps = conn.prepareStatement(query);
            // Set the search keyword with wildcard characters for partial matching
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Process each result row and map to Product object
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setCategory(rs.getString("category"));
                product.setSkinType(rs.getString("skin_type"));
                product.setKeyIngredients(rs.getString("key_ingredients"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setDescription(rs.getString("description"));
                product.setPhotoPath(rs.getString("photo_path"));
                // Add the product to the result list
                productList.add(product);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Return the list of matching products
        return productList;
    }

    // This method gets all products from the database including brand name
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, b.name as brand_name FROM products p "
                     + "LEFT JOIN brands b ON p.brand_id = b.id "
                     + "ORDER BY p.id DESC";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Loop through all products and add them to the list
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setCategory(rs.getString("category"));
                product.setSkinType(rs.getString("skin_type"));
                product.setKeyIngredients(rs.getString("key_ingredients"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setDescription(rs.getString("description"));
                product.setPhotoPath(rs.getString("photo_path"));
                products.add(product);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }

    // This method gets only products that are in stock from the database
    public List<Product> getInStockProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, b.name as brand_name FROM products p "
                     + "LEFT JOIN brands b ON p.brand_id = b.id "
                     + "WHERE p.stock_quantity >= 1";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Loop through all in stock products and add them to the list
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setCategory(rs.getString("category"));
                product.setSkinType(rs.getString("skin_type"));
                product.setKeyIngredients(rs.getString("key_ingredients"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setDescription(rs.getString("description"));
                product.setPhotoPath(rs.getString("photo_path"));
                products.add(product);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }

    // This method gets featured products for the home page (first 4 in stock products)
    public List<Product> getFeaturedProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, b.name as brand_name FROM products p "
                     + "LEFT JOIN brands b ON p.brand_id = b.id "
                     + "WHERE p.stock_quantity >= 1 LIMIT 4";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Loop through featured products and add them to the list
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setCategory(rs.getString("category"));
                product.setSkinType(rs.getString("skin_type"));
                product.setKeyIngredients(rs.getString("key_ingredients"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setDescription(rs.getString("description"));
                product.setPhotoPath(rs.getString("photo_path"));
                products.add(product);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }
}