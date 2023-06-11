package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
    public class SessionController {
        @GetMapping("/sessionData")
        public Map<String, Object> getSessionData(HttpSession session) {
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("username", session.getAttribute("username"));
            sessionData.put("authorities", session.getAttribute("authorities"));
            sessionData.put("userId", session.getAttribute("userId"));
            return sessionData;
        }
    }

