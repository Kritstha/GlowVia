package com.glowvia.service;

import com.glowvia.dao.UserDao;
import com.glowvia.model.User;
import com.glowvia.util.InputValidator;
import com.glowvia.util.PasswordHasher;

/**
 * Business logic for authentication and account creation.
 * Returns simple {@link AuthResult} objects so the servlet layer can show
 * either a user object or an error message without throwing.
 */
public class AuthService {

    private final UserDao userDao = new UserDao();

    public static class AuthResult {
        public final User user;
        public final String error;

        private AuthResult(User u, String e) { this.user = u; this.error = e; }

        public static AuthResult ok(User u)   { return new AuthResult(u, null); }
        public static AuthResult fail(String e) { return new AuthResult(null, e); }

        public boolean isOk() { return user != null; }
    }

    /* ------------------------------------------------------------------ */
    /* Login                                                              */
    /* ------------------------------------------------------------------ */
    public AuthResult login(String identifier, String password) {
        if (InputValidator.isBlank(identifier) || InputValidator.isBlank(password)) {
            return AuthResult.fail("Please provide username/email and password.");
        }

        User user = userDao.findByLogin(identifier.trim());
        if (user == null) {
            return AuthResult.fail("No account found for that username/email.");
        }
        if (!PasswordHasher.verify(password, user.getPassword())) {
            return AuthResult.fail("Incorrect password. Please try again.");
        }
        // Don't keep password hash in the session-bound model.
        user.setPassword(null);
        return AuthResult.ok(user);
    }

    /* ------------------------------------------------------------------ */
    /* Register                                                           */
    /* ------------------------------------------------------------------ */
    public AuthResult register(User input, String confirmPassword) {

        String err;
        if ((err = InputValidator.checkFullName(input.getFullName())) != null) return AuthResult.fail(err);
        if ((err = InputValidator.checkUsername(input.getUsername())) != null) return AuthResult.fail(err);
        if ((err = InputValidator.checkEmail(input.getEmail()))       != null) return AuthResult.fail(err);
        if ((err = InputValidator.checkPhone(input.getPhone()))       != null) return AuthResult.fail(err);
        if ((err = InputValidator.checkPassword(input.getPassword())) != null) return AuthResult.fail(err);

        if (!input.getPassword().equals(confirmPassword)) {
            return AuthResult.fail("Passwords do not match.");
        }

        if (userDao.usernameTaken(input.getUsername())) {
            return AuthResult.fail("That username is already in use.");
        }
        if (userDao.emailTaken(input.getEmail())) {
            return AuthResult.fail("An account with that email already exists.");
        }
        if (userDao.phoneTaken(input.getPhone())) {
            return AuthResult.fail("An account with that phone already exists.");
        }

        input.setPassword(PasswordHasher.hash(input.getPassword()));
        input.setRole("user");

        int newId = userDao.create(input);
        if (newId < 0) {
            return AuthResult.fail("Could not create your account. Please try again.");
        }
        input.setId(newId);
        input.setPassword(null);
        return AuthResult.ok(input);
    }
}
