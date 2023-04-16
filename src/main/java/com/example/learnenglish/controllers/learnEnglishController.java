package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class learnEnglishController {
    private final UserRepository userRepository;
    private final UserService userService;

    public learnEnglishController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    @GetMapping("/")
//    public String mainPage(Model model, Principal principal) {
//        model.addAttribute("title", "Learn English - Англійська за 16 годин.");
//        model.addAttribute("main_title", "Main page");
//        if(principal != null) {
//            UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
//            User user = userRepository.findByEmail(userDetails.getUsername());
//            model.addAttribute("user", user);
//        }
////        model.addAttribute("user", user);
//        return "index";
//    }
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

//    @GetMapping("/about-the-app")
//    public String aboutApp(@PathVariable("id") Long id, User user, Model model) {
//        id = user.getId();
//        model.addAttribute("title", "About the app Learn English");
//        model.addAttribute("main_title", "Main page");
//        model.addAttribute("user", user);
//        model.addAttribute("id", id);
//        return "about";
//    }
    @GetMapping("/about-the-app")
    public String aboutApp(Model model, Principal principal) {
        model.addAttribute("title", "About the app Learn English");
        model.addAttribute("main_title", "Main page");
        if (principal != null) {
            // Отримати id залогіненого користувача
            Long userId = userService.findByEmail(principal.getName()).getId();
            model.addAttribute("userId", userId);
            // Перенаправити на сторінку з профілем користувача з його id
            return "about";
        }
        return "about";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "About the app Learn English");
        model.addAttribute("main_title", "Login page");
        return "login";
    }

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Long userId, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        model.addAttribute("main_title", "User page");
        if (principal != null) {
            // Отримати id залогіненого користувача
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("userId", userId);
            model.addAttribute("user", user);
            return "user-info";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{id}/lesson/{lessonId}")
    public String lessonPage(@PathVariable("id") Long id, @PathVariable("lessonId") Long lessonId,
                             Lesson lesson, User user, Model model) {
        id = user.getId();
        lessonId = lesson.getId();
        model.addAttribute("title", "About the app Learn English");
        model.addAttribute("main_title", "Lesson page");
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("user", user);
        model.addAttribute("userId", id);

        return "lesson";
    }


}
