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
@RequestMapping(method = RequestMethod.GET)
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
    public String aboutApp(Model model, Principal principal) throws IOException {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            // Отримати id залогіненого користувача
            Long userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("avatarName",user.getUserAvatar().getAvatarName());
//            model.addAttribute("userId", userId);
//            model.addAttribute("userRole", user.getAuthority());
            model.addAttribute("user", user);
            return "about";
        }
        return "about";
    }
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }



    @PostMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model, Principal principal) {
        model.addAttribute("title", "About the app Learn English");
            if (error != null) {
                model.addAttribute("error", "Не вірний логін, або пароль!");
            } else if (logout != null) {
                model.addAttribute("logout", "Ви вийшли із системи.");
            }
        return "login";
    }

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Long userId, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("avatarName",user.getUserAvatar().getAvatarName());
            model.addAttribute("user", user);
            return "user-info";
        }
        return "redirect:/login";
    }
    @GetMapping("/user/{userId}/statistics")
    public String userStatisticsPage(@PathVariable("userId") Long userId, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("avatarName",user.getUserAvatar().getAvatarName());
            model.addAttribute("user", user);
            return "statistics";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{userId}/lesson/{lessonId}")
    public String lessonPage(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId,
                             Lesson lesson, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            lesson = lessonService.findById(lessonId);
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("avatarName",user.getUserAvatar().getAvatarName());
            model.addAttribute("user", user);
            model.addAttribute("lesson", lesson);
            return "lesson";
        }
        return "redirect:/login";
    }

    // ******************  Admin page  ********* //
    @GetMapping("/lessons/lesson/{id}/edit")
    public String lessonsEdit(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {

//        model.addAttribute("lesson", lesson);
        if (principal != null) {
//            id = lesson.getId();
//            lessonService.lessonSave(lesson);

            return "lesson-edit";
        }
        return "redirect:/login";
    }
}
