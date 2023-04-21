package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class learnEnglishController {

    private final UserService userService;

    public learnEnglishController(UserService userService) {
        this.userService = userService;
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
    public String aboutApp(Model model, Principal principal) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            // Отримати id залогіненого користувача
            Long userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("userId", userId);
            // Перенаправити на сторінку з профілем користувача з його id
            return "about";
        }
        return "about";
    }
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "About the app Learn English");
        return "login";
    }

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Long userId, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("userId", userId);
            model.addAttribute("user", user);
            return "user-info";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{userId}/lesson/{lessonId}")
    public String lessonPage(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId,
                             Lesson lesson, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("userId", userId);
            return "lesson";
        }
        lessonId = lesson.getId();
//        model.addAttribute(lesson);
        model.addAttribute("lessonId", lessonId);
        return "lesson";
    }


}
