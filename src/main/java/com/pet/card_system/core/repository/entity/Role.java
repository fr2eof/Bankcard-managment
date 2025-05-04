package com.pet.card_system.core.repository.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    /**
     * System Administrator - Full Access
     */
    ADMIN,

    /**
     * Standard User - Limited Rights
     */
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

    /**
     * Checks if the role is administrative
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
}
