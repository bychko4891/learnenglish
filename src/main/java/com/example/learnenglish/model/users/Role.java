package com.example.learnenglish.model.users;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_DEMO;

    @Override
    public String getAuthority() {
        return name();
    }
}
