package com.casbin.casbin_test.model;

public class DocumentPermission {
    private String userId;
    private String permission; // "read", "write", "delete", etc.

    public DocumentPermission() {}

    public DocumentPermission(String userId, String permission) {
        this.userId = userId;
        this.permission = permission;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
}
