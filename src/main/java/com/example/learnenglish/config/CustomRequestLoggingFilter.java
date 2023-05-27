package com.example.learnenglish.config;

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserStatisticsService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private UserService userService;


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && Pattern.matches("REQUEST : (GET|POST) /user/[0-9]+/lesson/[0-9]+.+", message)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                long userId = ((User) principal).getId();
                LocalDateTime timeStartUserActivity = LocalDateTime.now();
                userStatisticsService.learnUserTime(userId, timeStartUserActivity);

            }
//            User user = (User) authentication.getPrincipal();
//            System.out.println(user.toString());
//            System.out.println(message);
//            System.out.println(request + " afterRequest  залогінився ");
//            System.out.println(message);
         // не обробляйте запити незалогінених користувачів
        } else if (authentication != null && authentication.isAuthenticated() && Pattern.matches("REQUEST : GET /user/[0-9]+.+", message)){
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                long userId = ((User) principal).getId();
                String ipAddress = request.getRemoteAddr();
                userService.saveUserIp(userId, ipAddress);
                System.out.println("IP Address юзера: " + ipAddress);
            }
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
//        System.out.println(request + " afterRequest **********************************88");
        // Викликається після обробки запиту
        // Тут ви можете зберігати час закінчення запиту
    }
}
