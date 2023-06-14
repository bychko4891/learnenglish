package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Controller
//@RequestMapping(method = RequestMethod.GET)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration-page")
    public String registration() {
        return "registration";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (error != null) {
            model.addAttribute("error", "Не вірний логін, або пароль!");
        } else if (logout != null) {
            model.addAttribute("logout", "Ви вийшли із системи.");
        }
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(@PathVariable ("code")String code, Model model){
        boolean active = userService.activateUser(code);
        if(active){
            model.addAttribute("message", "Activate");
        } else model.addAttribute("message", "No activate");
        return "login";
    }
    @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "forgotPassword";
    }

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Long userId,
                           Principal principal,
                           Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            return "userInfo";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{userId}/statistics")
    public String userStatisticsPage(@PathVariable("userId") Long userId,
                                     Principal principal,
                                     Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            return "userStatistics";
        }
        return "redirect:/login";
    }
}
