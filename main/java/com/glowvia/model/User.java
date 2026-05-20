package com.glowvia.model;

/**
 * Model class representing a user account in Glowvia.
 * Mirrors the `users` table in the database.
 */
public class User {

    private int id;
    private String fullName;
    private String username;
    private String dob;
    private String gender;
    private String phone;
    private String email;
    private String password;
    private String imagePath;
    private String role;

    public User() {
    }

    public User(int id, String fullName, String username, String email,
                String role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
}
