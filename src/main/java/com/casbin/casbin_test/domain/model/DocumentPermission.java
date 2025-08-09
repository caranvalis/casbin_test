package com.casbin.casbin_test.domain.model;

public enum DocumentPermission {
    READ("read"),
    WRITE("write"),
    DELETE("delete"),
    EDIT("edit"),
    SHARE("share");

    private final String action;

    DocumentPermission(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return action;
    }
}