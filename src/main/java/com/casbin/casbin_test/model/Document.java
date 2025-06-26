package com.casbin.casbin_test.model;

import java.util.*;

public class Document {
    private String id;
    private String title;
    private String content;
    private String creatorId;
    private Date createdAt;
    private Date updatedAt;
    private List<DocumentPermission> permissions;
    // Ajout de l'attribut manquant
    private Map<String, String> sharedWith;

    private String categoryId;
    private User owner;

    public Document() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.permissions = new ArrayList<>();
        this.sharedWith = new HashMap<>();
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public List<DocumentPermission> getPermissions() { return permissions; }
    public void setPermissions(List<DocumentPermission> permissions) { this.permissions = permissions; }

    // Correction des méthodes pour sharedWith
    public Map<String, String> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(Map<String, String> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getCategoryId() {
     return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
    this.owner = owner;
    }
}