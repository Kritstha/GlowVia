package com.glowvia.service;

import com.glowvia.dao.UserDao;
import com.glowvia.model.User;
import com.glowvia.util.InputValidator;
import com.glowvia.util.PasswordHasher;

import java.util.List;

/**
  Profile management business logic.
 */
public class UserService {

    private final UserDao userDao = new UserDao();

    public User findById(int id) { return userDao.findById(id); }
    public List<User> listCustomers() { return userDao.listCustomers(); }

    public String updateProfile(User u) {
        String err;
        if ((err = InputValidator.checkFullName(u.getFullName())) != null) return err;
        if ((err = InputValidator.checkEmail(u.getEmail()))       != null) return err;
        if ((err = InputValidator.checkPhone(u.getPhone()))       != null) return err;
        return userDao.updateProfile(u) ? null : "Profile could not be saved.";
    }

    public String changePassword(int userId, String currentPlain,
                                 String newPlain, String confirm) {
        if (InputValidator.isBlank(currentPlain) || InputValidator.isBlank(newPlain)) {
            return "All password fields are required.";
        }
        if (!newPlain.equals(confirm)) {
            return "New passwords do not match.";
        }
        String err = InputValidator.checkPassword(newPlain);
        if (err != null) return err;

        User stored = userDao.findById(userId);
        if (stored == null) return "Account not found.";

        if (!PasswordHasher.verify(currentPlain, stored.getPassword())) {
            return "Current password is incorrect.";
        }

        return userDao.updatePassword(userId, PasswordHasher.hash(newPlain)) ? null
                : "Password could not be updated.";
    }
}
