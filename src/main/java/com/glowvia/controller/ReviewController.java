package com.glowvia.controller;

import com.glowvia.model.User;
import com.glowvia.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/review/add")
public class ReviewController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Service class that handles database operations for reviews
    private ReviewService reviewService = new ReviewService();

    // This method runs when user submits a review
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current user from the session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // If user is not logged in redirect to login page
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get the product id, rating and comment from the form
        String productIdParam = request.getParameter("productId");
        String ratingParam = request.getParameter("rating");
        String comment = request.getParameter("comment");

        // Check if product id is valid
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int rating = Integer.parseInt(ratingParam);

            // Check if rating is between 1 and 5
            if (rating < 1 || rating > 5) {
                session.setAttribute("error", "Rating must be between 1 and 5.");
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }

            // Check if user has already reviewed this product
            if (reviewService.hasUserReviewed(productId, currentUser.getId())) {
                session.setAttribute("error", "You have already reviewed this product.");
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }

            // Add the review to the database
            boolean isAdded = reviewService.addReview(productId, currentUser.getId(), rating, comment);

            // Check if review was added successfully
            if (isAdded) {
                session.setAttribute("success", "Review added successfully!");
            } else {
                session.setAttribute("error", "Failed to add review. Please try again.");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("error", "Something went wrong. Please try again.");
        }

        // Redirect back to the product page
        response.sendRedirect(request.getContextPath() + "/product?id=" + request.getParameter("productId"));
    }
}