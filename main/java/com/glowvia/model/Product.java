package com.glowvia.model;

/**
 * Model class representing a beauty product.
 * Mirrors the `products` table joined with `brands` for the brand label.
 */
public class Product {

    private int id;
    private String name;
    private int brandId;
    private String brandName;        
    private String category;
    private String skinType;
    private String keyIngredients;
    private double price;
    private int stockQuantity;
    private String description;
    private String photoPath;

    public Product() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSkinType() { return skinType; }
    public void setSkinType(String skinType) { this.skinType = skinType; }

    public String getKeyIngredients() { return keyIngredients; }
    public void setKeyIngredients(String keyIngredients) {
        this.keyIngredients = keyIngredients;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public boolean isInStock() {
        return this.stockQuantity > 0;
    }
}
