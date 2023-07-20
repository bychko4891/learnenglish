package com.example.learnenglish.model;

import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {

    private  boolean isActive;

    public UserContextHolder() {

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
