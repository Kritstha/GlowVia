package com.glowvia.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
 This controller handles the about page of the Glowvia website
  It is mapped to the /about URL so when user clicks About in the navbar
  this controller runs and shows the about us page
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/about" })
public class AboutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
      This method runs when the user visits the about page
     It sets the page title and forwards the request to the about JSP page
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the page title that will be shown in the browser tab
        request.setAttribute("pageTitle", "About Us - Glowvia");

        // Forward the request to the about JSP page to display it
        request.getRequestDispatcher("/pages/customer/about.jsp").forward(request, response);
    }
}