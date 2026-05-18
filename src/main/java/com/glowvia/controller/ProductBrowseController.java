package com.glowvia.controller;

import com.glowvia.model.Product;
import com.glowvia.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = { "/products" })
public class ProductBrowseController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when user visits the products page
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Create product service to get products from database
        ProductService productService = new ProductService();

        // Get the search keyword from the search form
        String keyword = request.getParameter("q");

        List<Product> products;

        // If keyword is not empty search for products by name brand or ingredients
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Search products by name, brand or ingredients
            products = productService.searchProducts(keyword.trim());
            // Pass the keyword back to keep it in the search box
            request.setAttribute("searchKeyword", keyword);
        } else {
            // If no keyword get all products from the database
            products = productService.getAllProducts();
        }

        // Pass the products list to the JSP page
        request.setAttribute("products", products);
        request.setAttribute("pageTitle", "Shop - Glowvia");

        // Forward to the products page
        request.getRequestDispatcher("/pages/customer/products.jsp").forward(request, response);
    }
}