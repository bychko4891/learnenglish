package com.example.learnenglish.configurations;

import com.example.learnenglish.model.users.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && Pattern.matches("REQUEST : (GET|POST) /user/[0-9]+/lesson/[0-9]+.+", message)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                Long userId = ((User) principal).getId();
                LocalDateTime timeStartUserActivity = LocalDateTime.now();

                // використовуйте userId
            }
            User user = (User) authentication.getPrincipal();
//            System.out.println(user.toString());
            System.out.println(message);
//            System.out.println(request + " afterRequest  залогінився ");
//            System.out.println(message);
         if (request.getUserPrincipal() != null) {
//            System.out.println(request + " afterRequest  залогінився **********************************88");
        }
        } else {
            // не обробляйте запити незалогінених користувачів
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
//        System.out.println(request + " afterRequest **********************************88");
        // Викликається після обробки запиту
        // Тут ви можете зберігати час закінчення запиту
    }
}