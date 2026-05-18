package com.glowvia.controller;

import com.glowvia.model.Product;
import com.glowvia.model.User;
import com.glowvia.service.ProductService;
import com.glowvia.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/product")
public class ProductDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when user clicks on a product to view its details
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the product id from the URL parameter
        String productIdParam = request.getParameter("id");

        // If no product id provided redirect to products page
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            // Convert the product id from string to integer
            int productId = Integer.parseInt(productIdParam);

            // Create product service to get product from database
            ProductService productService = new ProductService();

            // Get the product by its id from the database
            Product selectedProduct = productService.getProductById(productId);

            // If product found show the product detail page
            if (selectedProduct != null) {

                // Get reviews for this product from the database
                ReviewService reviewService = new ReviewService();
                List<Map<String, Object>> reviews = reviewService.getReviewsByProductId(productId);

                // Check if current user is logged in
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("currentUser");

                // Check if user has already reviewed this product
                boolean hasReviewed = false;
                if (currentUser != null) {
                    hasReviewed = reviewService.hasUserReviewed(productId, currentUser.getId());
                }

                // Pass all data to the product detail page
                request.setAttribute("product", selectedProduct);
                request.setAttribute("reviews", reviews);
                request.setAttribute("hasReviewed", hasReviewed);
                request.setAttribute("pageTitle", selectedProduct.getName() + " - Glowvia");

                request.getRequestDispatcher("/pages/customer/product_detail.jsp").forward(request, response);

            } else {
                // If product not found redirect to products page
                response.sendRedirect(request.getContextPath() + "/products");
            }

        } catch (NumberFormatException e) {
            // If product id is not a valid number redirect to products page
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}