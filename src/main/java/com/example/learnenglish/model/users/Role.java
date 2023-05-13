package com.example.learnenglish.model.users;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, DEMO;

    @Override
    public String getAuthority() {
        return name();
    }
}
