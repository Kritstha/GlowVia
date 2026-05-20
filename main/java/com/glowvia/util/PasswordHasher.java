package com.glowvia.util;

import org.mindrot.jbcrypt.BCrypt;

/**
  Password hashing wrapper around jBCrypt.
  Use {@link #hash(String)} when storing a new password and
  {@link #verify(String, String)} when checking a login attempt.
 */
public final class PasswordHasher {

    /** Work factor for BCrypt. Higher = stronger but slower. */
    private static final int WORK_FACTOR = 10;

    private PasswordHasher() {
    }

    public static String hash(String plainText) {
        if (plainText == null) {
            return null;
        }
        return BCrypt.hashpw(plainText, BCrypt.gensalt(WORK_FACTOR));
    }

    public static boolean verify(String plainText, String storedHash) {
        if (plainText == null || storedHash == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainText, storedHash);
        } catch (IllegalArgumentException ex) {
            // Handle legacy plain-text values gracefully
            return plainText.equals(storedHash);
        }
    }
}
