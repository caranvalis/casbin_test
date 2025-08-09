package com.casbin.casbin_test.domain.model;

public class Resource {
    private String owner;
    private String name;

    public Resource(String owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }
}