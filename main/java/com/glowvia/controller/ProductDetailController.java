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
This controller handles product detail page requests.
It displays full product information along with reviews.
*/
@WebServlet("/product")
public class ProductDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String productIdParam = request.getParameter("id");

        /*
        Validate product id before processing request.
        Redirect user if product id is missing.
        */        
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            
            int productId = Integer.parseInt(productIdParam);
            ProductService productService = new ProductService();           
            Product selectedProduct = productService.getProductById(productId);

            
            if (selectedProduct != null) {

                ReviewService reviewService = new ReviewService();
                List<Map<String, Object>> reviews = reviewService.getReviewsByProductId(productId);

                
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("currentUser");

              
                boolean hasReviewed = false;
                if (currentUser != null) {
                    hasReviewed = reviewService.hasUserReviewed(productId, currentUser.getId());
                }

//send product details and review data to jsp page for display                
                request.setAttribute("product", selectedProduct);
                request.setAttribute("reviews", reviews);
                request.setAttribute("hasReviewed", hasReviewed);
                request.setAttribute("pageTitle", selectedProduct.getName() + " - Glowvia");

                
                request.getRequestDispatcher("/pages/customer/product_detail.jsp").forward(request, response);

            } else {
//Redirect user if product is not found in database                
                response.sendRedirect(request.getContextPath() + "/products");
            }
//Handle invalid product id format and prevent application crash.
        } catch (NumberFormatException e) {
            
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
