package com.glowvia.model;

import java.sql.Timestamp;

/**
 * Model class representing a beauty brand.
 * Mirrors the `brands` table.
 */
public class Brand {

    private int id;
    private String name;
    private String contact;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Brand() {
    }

    public Brand(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
