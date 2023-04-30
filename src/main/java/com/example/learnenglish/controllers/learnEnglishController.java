package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class learnEnglishController {
    private final ResourceLoader resourceLoader;
    private final UserService userService;

    public learnEnglishController(ResourceLoader resourceLoader, UserService userService) {
        this.resourceLoader = resourceLoader;
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
    public String aboutApp(Model model, Principal principal) throws IOException {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            // Отримати id залогіненого користувача
            Long userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("userId", userId);
//            Resource resource = resourceLoader.getResource("/home/anatolii/Documents/learnEnglishImages" + user.getAvatarePath());
//            model.addAttribute("avatareUser", resource.getURL().getPath());
            // Перенаправити на сторінку з профілем користувача з його id
            return "about";
        }
        return "about";
    }
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


//    @GetMapping("/login")
//    public String loginPage(Model model) {
//        model.addAttribute("title", "About the app Learn English");
//        return "login";
//    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
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
//            user.getUserAvatar().getAvatarName();
            model.addAttribute("avatarName",user.getUserAvatar().getAvatarName());
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
//            model.addAttribute("avatareUser",user.getAvatarePath());
            model.addAttribute("userId", userId);
            model.addAttribute("user", user);
            return "lesson";
        }
        lessonId = lesson.getId();
//        model.addAttribute(lesson);
        model.addAttribute("lessonId", lessonId);
        return "lesson";
    }
    @GetMapping("/user/{id}/statistics")
    public String userPageStatistics(@PathVariable("id") Long userId, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("userId", userId);
            model.addAttribute("user", user);
            return "statistics";
        }
        return "redirect:/login";
    }
    @GetMapping("/upload")
    public String file( Model model) {
            model.addAttribute("title", "About the app Learn English");
            model.addAttribute("main_title", "Main page");
            // Перенаправити на сторінку з увійти / зареєструватися
            return "filetest";

    }
}
