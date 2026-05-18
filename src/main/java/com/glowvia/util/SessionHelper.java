package com.glowvia.util;

import com.glowvia.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Tiny convenience layer over HttpSession to read/write the logged-in user
 * and short-lived flash messages used between redirects.
 */
public final class SessionHelper {

    public static final String USER_ATTR        = "currentUser";
    public static final String CART_COUNT_ATTR  = "cartCount";
    public static final String FLASH_OK_ATTR    = "flashSuccess";
    public static final String FLASH_ERR_ATTR   = "flashError";

    private SessionHelper() {
    }

    public static void setUser(HttpServletRequest req, User user) {
        req.getSession(true).setAttribute(USER_ATTR, user);
    }

    public static User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        Object obj = session.getAttribute(USER_ATTR);
        return (obj instanceof User) ? (User) obj : null;
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        return getUser(req) != null;
    }

    public static boolean isAdmin(HttpServletRequest req) {
        User user = getUser(req);
        return user != null && user.isAdmin();
    }

    public static void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static void flashSuccess(HttpServletRequest req, String msg) {
        req.getSession(true).setAttribute(FLASH_OK_ATTR, msg);
    }

    public static void flashError(HttpServletRequest req, String msg) {
        req.getSession(true).setAttribute(FLASH_ERR_ATTR, msg);
    }

    public static void setCartCount(HttpServletRequest req, int count) {
        req.getSession(true).setAttribute(CART_COUNT_ATTR, count);
    }
}
