package com.example.learnenglish.controllers;

import com.example.learnenglish.model.users.UserStatistics;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.UserStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserStatisticsController {
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;

    public UserStatisticsController(UserStatisticsService userStatisticsService, UserService userService) {
        this.userStatisticsService = userStatisticsService;
        this.userService = userService;
    }

    @GetMapping("/training-day")
    public ResponseEntity<List> calendarDays(Principal principal){
        if (principal != null) {
          Long  userId = userService.findByEmail(principal.getName()).getId();
                     return ResponseEntity.ok(userStatisticsService.trainingDays(userId));

        }
        return ResponseEntity.notFound().build();
    }
}
