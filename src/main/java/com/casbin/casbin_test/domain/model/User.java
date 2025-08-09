package com.casbin.casbin_test.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String role;
    private boolean isAdmin;

    // Constructeur par défaut
    public User() {}

    // Constructeur pour les tests de document et la persistence en BD
    public User(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.isAdmin = "ADMIN".equalsIgnoreCase(role);
    }

    // Constructeur pour ABAC
    public User(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.role = isAdmin ? "ADMIN" : "USER";
        this.email = username + "@example.com"; // Valeur par défaut pour les cas ABAC
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.isAdmin = "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        if (admin && !"ADMIN".equalsIgnoreCase(role)) {
            this.role = "ADMIN";
        }
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getName() {
        return this.username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}