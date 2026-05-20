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

/*
 This controller handles the home page
 It shows featured products from the database on the home page
*/
@WebServlet(asyncSupported = true, urlPatterns = { "/home", "/" })
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public HomeController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip static files like images and css
        if (path.startsWith("/images/") || path.startsWith("/css/") || path.startsWith("/uploads/")) {
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }

        if (path.equals("/home") || path.equals("/")) {

            /*
             Get featured products from the database and pass them to the home page
             Only products that are in stock are shown as featured products
            */
            ProductService productService = new ProductService();
            List<Product> featuredProducts = productService.getFeaturedProducts();

            request.setAttribute("featuredProducts", featuredProducts);
            request.setAttribute("pageTitle", "Glowvia - Glow with Confidence");
            request.getRequestDispatcher("/pages/customer/home.jsp").forward(request, response);

        } else {
            // If path is not found show 404 error page
            request.getRequestDispatcher("/pages/error/404.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}