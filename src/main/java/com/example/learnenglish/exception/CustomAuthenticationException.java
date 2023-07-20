package com.example.learnenglish.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class CustomAuthenticationException extends AuthenticationServiceException {

    public CustomAuthenticationException(String message) {
        super(message);
    }
}
