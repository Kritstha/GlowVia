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

/**
 * Filter that enforces authentication and role-based access on protected URLs.
 * Public paths (home, login, register, browse) are passed through, everything
 * else demands a session, and admin URLs additionally require the admin role.
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/", "/home", "/login", "/register", "/logout",
            "/products", "/product", "/about", "/contact"
    );

    private static final Set<String> PUBLIC_PREFIXES = Set.of(
            "/css/", "/images/", "/uploads/", "/pages/error/", "/favicon"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ctx  = request.getContextPath();
        String path = request.getRequestURI().substring(ctx.length());

        if (isPublic(path)) {
            chain.doFilter(req, res);
            return;
        }

        if (!SessionHelper.isLoggedIn(request)) {
            SessionHelper.flashError(request,
                    "Please sign in to continue.");
            response.sendRedirect(ctx + "/login");
            return;
        }

        if (path.startsWith("/admin") && !SessionHelper.isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required.");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isPublic(String path) {
        if (PUBLIC_PATHS.contains(path)) return true;
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        return false;
    }
}
