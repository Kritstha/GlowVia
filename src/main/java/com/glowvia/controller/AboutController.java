package com.glowvia.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(asyncSupported = true, urlPatterns = { "/about" })
public class AboutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when user visits the about page
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the page title
        request.setAttribute("pageTitle", "About Us - Glowvia");

        // Forward to the about page
        request.getRequestDispatcher("/pages/customer/about.jsp").forward(request, response);
    }
}