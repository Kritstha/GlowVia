package com.glowvia.util;

import java.util.regex.Pattern;

/**
 * Reusable input validation routines. Returns a human-readable error message
 * when invalid, or {@code null} when the value is acceptable.
 */
public final class InputValidator {

    private static final Pattern EMAIL_RE =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_RE =
            Pattern.compile("^\\+?[0-9 -]{7,20}$");

    private static final Pattern USERNAME_RE =
            Pattern.compile("^[A-Za-z0-9_.]{3,30}$");

    private static final Pattern NAME_RE =
            Pattern.compile("^[A-Za-z][A-Za-z .'-]{1,99}$");

    private InputValidator() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String checkFullName(String value) {
        if (isBlank(value)) return "Full name is required.";
        if (!NAME_RE.matcher(value.trim()).matches()) {
            return "Full name must start with a letter and contain only letters and spaces.";
        }
        return null;
    }

    public static String checkUsername(String value) {
        if (isBlank(value)) return "Username is required.";
        if (!USERNAME_RE.matcher(value.trim()).matches()) {
            return "Username must be 3-30 characters: letters, digits, dot, or underscore.";
        }
        return null;
    }

    public static String checkEmail(String value) {
        if (isBlank(value)) return "Email is required.";
        if (!EMAIL_RE.matcher(value.trim()).matches()) {
            return "Please enter a valid email address.";
        }
        return null;
    }

    public static String checkPhone(String value) {
        if (isBlank(value)) return "Phone number is required.";
        if (!PHONE_RE.matcher(value.trim()).matches()) {
            return "Phone must contain 7-20 digits (and may start with +).";
        }
        return null;
    }

    public static String checkPassword(String value) {
        if (isBlank(value)) return "Password is required.";
        if (value.length() < 6) return "Password must be at least 6 characters long.";
        return null;
    }

    public static String checkPositiveNumber(String value, String label) {
        if (isBlank(value)) return label + " is required.";
        try {
            double d = Double.parseDouble(value.trim());
            if (d <= 0) return label + " must be greater than zero.";
        } catch (NumberFormatException ex) {
            return label + " must be a valid number.";
        }
        return null;
    }

    public static String checkNonNegativeInt(String value, String label) {
        if (isBlank(value)) return label + " is required.";
        try {
            int i = Integer.parseInt(value.trim());
            if (i < 0) return label + " cannot be negative.";
        } catch (NumberFormatException ex) {
            return label + " must be a whole number.";
        }
        return null;
    }

    public static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * Escape unsafe HTML characters - used by the view-helper to embed user
     * supplied text into a pre-rendered HTML fragment safely.
     */
    public static String escapeHtml(String value) {
        if (value == null) return "";
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '&':  out.append("&amp;");  break;
                case '<':  out.append("&lt;");   break;
                case '>':  out.append("&gt;");   break;
                case '"':  out.append("&quot;"); break;
                case '\'': out.append("&#39;");  break;
                default:   out.append(c);
            }
        }
        return out.toString();
    }
}
