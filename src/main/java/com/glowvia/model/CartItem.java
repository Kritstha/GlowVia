package com.glowvia.model;

/**
 * Composite view-model for an item in a user's cart, enriched with product
 * details. Used when rendering the cart page so that the JSP can use EL only
 * without joining lists at the view layer.
 */
public class CartItem {

    private int cartItemId;
    private int cartId;
    private int productId;
    private int quantity;

    // Joined product fields
    private String productName;
    private String brandName;
    private double price;
    private String photoPath;
    private int stockQuantity;

    public CartItem() {
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getLineTotal() {
        return this.price * this.quantity;
    }
}
