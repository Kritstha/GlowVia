package com.glowvia.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/*
 This controller handles the logout process
 It destroys the session and redirects to the login page
*/
@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         Get the current session without creating a new one
         If session exists invalidate it to log the user out
        */
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }


        response.sendRedirect(request.getContextPath() + "/login");
    }
}