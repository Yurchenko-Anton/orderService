package com.example.action.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    DRIVER(Set.of(Permission.USERS_DRIVER)),
    PASSENGER(Set.of(Permission.USERS_PASSENGER)),
    ADMIN(Set.of(Permission.USERS_ADMIN, Permission.USERS_PASSENGER, Permission.USERS_DRIVER));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}