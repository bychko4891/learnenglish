package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.CustomAuthenticationException;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.UserContextHolder;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.PageApplicationService;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PageApplicationService pageApplicationService;
    private final UserContextHolder userContextHolder;
    private final WordService wordService;


    @GetMapping("/registration-page")
    public String registration(Model model) {
        model.addAttribute("title", "About the app Learn English");
        PageApplication pageApplication = pageApplicationService.getPageApplication(2l);
        if (pageApplication.getTextOfAppPage() != null) {
            model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
        } else {
            model.addAttribute("pageText", "No text in this page");
        }
        return "registration";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) throws CustomAuthenticationException {
        model.addAttribute("title", "About the app Learn English");
        PageApplication pageApplication = pageApplicationService.getPageApplication(1l);
        if (pageApplication.getTextOfAppPage() != null) {
            model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
        } else {
            model.addAttribute("pageText", "No text in this page");
        }
        if (error != null) {
            model.addAttribute("error", "Не вірний логін, або пароль!");
            if(!userContextHolder.isActive()){
            model.addAttribute("error", "Пройдіть будь ласка верифікацію Email");
            }
        } else if (logout != null) {
            model.addAttribute("logout", "Ви вийшли із системи.");
        }
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(@PathVariable("code") String code, Model model) {
        boolean active = userService.activateUser(code);
        if (active) {
            model.addAttribute("message", "Activate");
        } else model.addAttribute("message", "No activate");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
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

    @GetMapping("/user/words")
    public String getUserWords(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                               Principal principal,
                               Model model)  {
        if (principal != null) {
            if (page < 0) page = 0;
            Long userId = userService.findByEmail(principal.getName()).getId();
            Page<Word> words = wordService.getUserWords(page, size, userId);
            model.addAttribute("words",words);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", words.getTotalPages());
            return "userWords";
        } else return "redirect:/login";
    }

//    @ExceptionHandler(CustomAuthenticationException.class)
//    public String handleFileFormatException(CustomAuthenticationException ex, Model model) {
//        model.addAttribute("message", ex.getMessage());
//        return "login";
//    }
}
