package com.glowvia.filter;

import com.glowvia.util.SessionHelper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

// This filter checks if the user is logged in and has the right role to access a page
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    // These paths are accessible without logging in
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/", "/home", "/login", "/register", "/logout",
            "/products", "/product", "/about", "/contact"
    );

    // These path prefixes are accessible without logging in
    private static final Set<String> PUBLIC_PREFIXES = Set.of(
            "/css/", "/images/", "/uploads/", "/pages/error/", "/favicon"
    );

    // This method runs on every request to check access
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Get the context path and requested path
        String ctx = request.getContextPath();
        String path = request.getRequestURI().substring(ctx.length());

        // If path is public let the request through
        if (isPublic(path)) {
            chain.doFilter(req, res);
            return;
        }

        // If user is not logged in redirect to login page
        if (!SessionHelper.isLoggedIn(request)) {
            SessionHelper.flashError(request, "Please sign in to continue.");
            response.sendRedirect(ctx + "/login");
            return;
        }

        // If path is admin and user is not admin redirect to access denied page
        if (path.startsWith("/admin") && !SessionHelper.isAdmin(request)) {
            response.sendRedirect(ctx + "/pages/error/access-denied.jsp");
            return;
        }

        // Otherwise let the request through
        chain.doFilter(req, res);
    }

    // This method checks if the path is public
    private boolean isPublic(String path) {
        if (PUBLIC_PATHS.contains(path)) return true;
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        return false;
    }
}