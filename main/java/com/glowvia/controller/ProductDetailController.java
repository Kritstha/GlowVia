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

/*
  This controller handles the product detail page
  It is mapped to the /product URL
  When customer clicks on a product it shows the full details
  including description, price, reviews and add to cart form
 */
@WebServlet("/product")
public class ProductDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
      This method runs when the customer clicks on a product to view its details
      It gets the product from the database along with its reviews
      and checks if the current user has already reviewed this product
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String productIdParam = request.getParameter("id");

        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            ProductService productService = new ProductService();

            // Get the product by its id from the database
            Product selectedProduct = productService.getProductById(productId);

            // If product found show the product detail page
            if (selectedProduct != null) {

                /*
                  Get all reviews for this product from the database
                  Reviews are shown at the bottom of the product detail page
                 */
                ReviewService reviewService = new ReviewService();
                List<Map<String, Object>> reviews = reviewService.getReviewsByProductId(productId);

                // Get the current user from the session
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("currentUser");

                /*
                  Check if the current user has already reviewed this product
                  If yes the review form will be hidden so they cannot review twice
                 */
                boolean hasReviewed = false;
                if (currentUser != null) {
                    hasReviewed = reviewService.hasUserReviewed(productId, currentUser.getId());
                }

                // Set the product, reviews and hasReviewed as request attributes
                request.setAttribute("product", selectedProduct);
                request.setAttribute("reviews", reviews);
                request.setAttribute("hasReviewed", hasReviewed);
                request.setAttribute("pageTitle", selectedProduct.getName() + " - Glowvia");

                // Forward to the product detail JSP page to display everything
                request.getRequestDispatcher("/pages/customer/product_detail.jsp").forward(request, response);

            } else {
                // If product not found redirect to the products page
                response.sendRedirect(request.getContextPath() + "/products");
            }

        } catch (NumberFormatException e) {
            // If product id is not a valid number redirect to the products page
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}