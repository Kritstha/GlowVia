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

/*
 * This controller handles the customer review submission
 * It is mapped to the /review/add URL
 * Logged in customers can submit a review for a product they have purchased
 */
@WebServlet("/review/add")
public class ReviewController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
     * This is the review service class
     * It handles all database operations related to reviews
     */
    private ReviewService reviewService = new ReviewService();

    /*
     * This method runs when the customer submits a review form
     * It validates the review and saves it to the database
     * Each customer can only leave one review per product
     */
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

        // Get the product id, rating and comment from the review form
        String productIdParam = request.getParameter("productId");
        String ratingParam = request.getParameter("rating");
        String comment = request.getParameter("comment");

        // If no product id provided redirect to products page
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int rating = Integer.parseInt(ratingParam);

            // Check if rating is a valid number between 1 and 5
            if (rating < 1 || rating > 5) {
                session.setAttribute("error", "Rating must be between 1 and 5.");
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }

            /*
             * Check if the customer has already reviewed this product
             * Each customer is only allowed to leave one review per product
             */
            if (reviewService.hasUserReviewed(productId, currentUser.getId())) {
                session.setAttribute("error", "You have already reviewed this product.");
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }

            // Add the review to the database using the review service
            boolean isAdded = reviewService.addReview(productId, currentUser.getId(), rating, comment);

            // Check if review was added successfully and set flash message
            if (isAdded) {
                session.setAttribute("success", "Review added successfully!");
            } else {
                session.setAttribute("error", "Failed to add review. Please try again.");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("error", "Something went wrong. Please try again.");
        }

        // Redirect back to the product page after review is submitted
        response.sendRedirect(request.getContextPath() + "/product?id=" + request.getParameter("productId"));
    }
}