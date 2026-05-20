package com.glowvia.controller;

import com.glowvia.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
 This controller handles the contact page
 Customer can send a message to Glowvia by filling in the contact form
*/
@WebServlet(asyncSupported = true, urlPatterns = { "/contact" })
public class ContactController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Contact Us - Glowvia");
        request.getRequestDispatcher("/pages/customer/contact.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        // Validate name, email and message before processing
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Please enter your name.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        if (message == null || message.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Please enter your message.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            request.getSession().setAttribute("error", "Please enter a valid email address.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        /*
         If all fields are valid show success message and redirect back to contact page
         In a real system this would send an email to the Glowvia support team
        */
        request.getSession().setAttribute("success", "Thanks " + name.trim()
            + " - we have received your message and will reply within two business days.");
        response.sendRedirect(request.getContextPath() + "/contact");
    }
}