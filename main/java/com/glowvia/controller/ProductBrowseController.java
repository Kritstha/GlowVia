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
 This controller handles the products browse page
 Customers can view all products or search by name, brand or ingredients
*/
@WebServlet(asyncSupported = true, urlPatterns = { "/products" })
public class ProductBrowseController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductService productService = new ProductService();
        String keyword = request.getParameter("q");
        List<Product> products;

        /*
         If keyword is entered search for matching products
         Otherwise get all products from the database
        */
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword.trim());
            request.setAttribute("searchKeyword", keyword.trim());
        } else {
            products = productService.getAllProducts();
        }

        request.setAttribute("products", products);
        request.setAttribute("pageTitle", "Shop - Glowvia");
        request.getRequestDispatcher("/pages/customer/products.jsp").forward(request, response);
    }
}