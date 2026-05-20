package com.glowvia.service;

import com.glowvia.dao.BrandDao;
import com.glowvia.model.Brand;
import com.glowvia.util.InputValidator;

import java.util.List;

/**
  Business logic for brand management (admin-side).
 */
public class BrandService {

    private final BrandDao brandDao = new BrandDao();

    public List<Brand> listAll() {
        return brandDao.listAll();
    }

    public String add(String name, String contact) {
        if (InputValidator.isBlank(name)) {
            return "Brand name is required.";
        }
        name = name.trim();
        if (name.length() > 255) {
            return "Brand name is too long.";
        }
        if (brandDao.nameExists(name)) {
            return "That brand already exists.";
        }
        Brand b = new Brand();
        b.setName(name);
        b.setContact(contact == null ? "" : contact.trim());
        return brandDao.insert(b) ? null : "Failed to add brand.";
    }

    public String delete(int id) {
        if (id <= 0) return "Invalid brand id.";
        return brandDao.delete(id) ? null
                : "Could not delete this brand (it may still have products).";
    }
}
