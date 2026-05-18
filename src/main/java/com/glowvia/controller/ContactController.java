package com.glowvia.controller;

import com.glowvia.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(asyncSupported = true, urlPatterns = { "/contact" })
public class ContactController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // This method runs when user visits the contact page
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the page title
        request.setAttribute("pageTitle", "Contact Us - Glowvia");

        // Forward to the contact page
        request.getRequestDispatcher("/pages/customer/contact.jsp").forward(request, response);
    }

    // This method runs when user submits the contact form
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the form fields from the contact form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        // Check if name is empty
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Please enter your name.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        // Check if message is empty
        if (message == null || message.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Please enter your message.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        // Check if email format is valid
        if (!ValidationUtil.isValidEmail(email)) {
            request.getSession().setAttribute("error", "Please enter a valid email address.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        // Show success message after form is submitted
        request.getSession().setAttribute("success", "Thanks " + name.trim() 
            + " - we have received your message and will reply within two business days.");

        // Redirect back to contact page
        response.sendRedirect(request.getContextPath() + "/contact");
    }
}