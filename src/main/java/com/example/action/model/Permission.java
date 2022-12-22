package com.example.action.model;

public enum Permission {
    USERS_PASSENGER("users:passenger"),
    USERS_DRIVER("users:driver"),
    USERS_ADMIN("users:admin");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
