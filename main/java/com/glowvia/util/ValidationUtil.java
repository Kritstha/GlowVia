package com.glowvia.util;

import jakarta.servlet.http.Part;

public class ValidationUtil {

    // This method checks if the full name is valid (letters and spaces only)
    public static boolean isValidFullName(String fullName) {
        return fullName != null
            && !fullName.trim().isEmpty()
            && fullName.matches("[a-zA-Z ]+");
    }

    // This method checks if the username is valid
    public static boolean isValidUsername(String username) {
        return username != null
            && username.length() > 4
            && !username.contains(" ")
            && !Character.isDigit(username.charAt(0));
    }

    // This method checks if the email format is valid
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    // This method checks if the password is valid
    public static boolean isValidPassword(String password) {
        return password != null && password.length() > 4;
    }

    // This method checks if the phone number is valid
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    // This method checks if the uploaded image is valid
    public static boolean isValidImage(Part part) {
        if (part == null || part.getSize() == 0) {
            return true;
        }
        long maxSize = 2 * 1024 * 1024; // 2MB
        String fileName = part.getSubmittedFileName();
        String lowerName = fileName.toLowerCase();
        return part.getSize() <= maxSize && (
            lowerName.endsWith(".png") ||
            lowerName.endsWith(".jpg") ||
            lowerName.endsWith(".jpeg") ||
            lowerName.endsWith(".gif")
        );
    }
}