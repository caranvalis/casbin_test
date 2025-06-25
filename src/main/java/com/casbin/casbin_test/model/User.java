package com.casbin.casbin_test.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private boolean isAdmin;

    // Constructeur explicite avec tous les arguments
    public User(String name, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    // Constructeur sans argument explicite
    public User() {
        this.id = "";
        this.name = "";
        this.isAdmin = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setId(String id) {
    }

    public void setUsername(String admin) {
    }

    public void setRole(String admin) {
    }
}
