package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.UserService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;

@Controller
//@RequestMapping(method = RequestMethod.GET)
public class learnEnglishController {
    private final ResourceLoader resourceLoader;
    private final UserService userService;
    private final LessonService lessonService;

    public learnEnglishController(ResourceLoader resourceLoader, UserService userService, LessonService lessonService) {
        this.resourceLoader = resourceLoader;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            // Отримати id залогіненого користувача
            Long userId = userService.findByEmail(principal.getName()).getId();
            model.addAttribute("userId", userId);
            // Перенаправити на сторінку з профілем користувача з його id
            return "redirect:/user/" + userId;
        } else {
            model.addAttribute("title", "About the app Learn English");
            model.addAttribute("main_title", "Main page");
            // Перенаправити на сторінку з увійти / зареєструватися
            return "index";
        }
    }



    @GetMapping("/about-the-app")
    public String aboutApp(Model model) {
        model.addAttribute("title", "About the app Learn English");
        return "about-the-app";
    }


    @GetMapping("/user/{userId}/lesson/{lessonId}")
    public String lessonPage(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId,
                             Lesson lesson, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            lesson = lessonService.findById(lessonId);
            model.addAttribute("lessonId", lesson.getId());
            model.addAttribute("lesson", lesson);
            model.addAttribute("userId", userId);
            return "lesson";
        }
        return "redirect:/login";
    }
//    @RequestMapping("/website")
//    public String loadContent() {
//        return "website";
//    }
}
