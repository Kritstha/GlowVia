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

@WebServlet(asyncSupported = true, urlPatterns = { "/home", "/" })
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public HomeController() {
        super();
    }

    // This method runs when user visits the home page
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the requested path
        String path = request.getServletPath();

        // Skip static files like images and css
        if (path.startsWith("/images/") || path.startsWith("/css/") || path.startsWith("/uploads/")) {
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }

        // Show the home page with featured products
        if (path.equals("/home") || path.equals("/")) {
            // Get all products from the database using ProductService
            ProductService productService = new ProductService();
            List<Product> featuredProducts = productService.getFeaturedProducts();

            // Pass the products to the home page
            request.setAttribute("featuredProducts", featuredProducts);
            request.setAttribute("pageTitle", "Glowvia - Glow with Confidence");
            request.getRequestDispatcher("/pages/customer/home.jsp").forward(request, response);
        }
        else {
            // If path is not found show error page
            request.getRequestDispatcher("/pages/error/404.jsp").forward(request, response);
        }
    }

    // This method handles POST requests by calling doGet
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}