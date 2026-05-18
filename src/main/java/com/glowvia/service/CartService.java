package com.glowvia.service;

import com.glowvia.dao.CartDao;
import com.glowvia.dao.ProductDao;
import com.glowvia.model.CartItem;
import com.glowvia.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cart-related business logic.
 */
public class CartService {

    private final CartDao cartDao = new CartDao();
    private final ProductDao productDao = new ProductDao();

    public int ensureCart(int userId) { return cartDao.ensureCart(userId); }

    public List<CartItem> items(int cartId) { return cartDao.listItems(cartId); }

    public int countItems(int userId) { return cartDao.countItems(userId); }

    public String addToCart(int userId, int productId, int qty) {
        if (qty <= 0) qty = 1;
        Product p = productDao.findById(productId);
        if (p == null) return "Product not found.";
        if (p.getStockQuantity() < qty) {
            return "Sorry, only " + p.getStockQuantity()
                    + " left in stock.";
        }
        int cartId = cartDao.ensureCart(userId);
        if (cartId < 0) return "Cart could not be created.";
        return cartDao.addOrIncrement(cartId, productId, qty) ? null
                : "Could not add item to cart.";
    }

    public String updateQuantity(int cartItemId, int qty) {
        if (qty <= 0) {
            return cartDao.removeItem(cartItemId) ? null : "Could not remove item.";
        }
        return cartDao.updateQuantity(cartItemId, qty) ? null
                : "Could not update item.";
    }

    public String removeItem(int cartItemId) {
        return cartDao.removeItem(cartItemId) ? null : "Could not remove item.";
    }

    public BigDecimal totalOf(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items == null) return total;
        for (CartItem ci : items) {
            total = total.add(BigDecimal.valueOf(ci.getLineTotal()));
        }
        return total.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
