package com.example.learnenglish.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class learnEnglishController {
    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("title", "Learn English - Англійська за 16 годин.");
        model.addAttribute("main_title", "Main page");
        return "index";
    }
    @GetMapping("/about-the-app")
    public String aboutApp(Model model) {
        model.addAttribute("title", "About the app Learn English");
        model.addAttribute("main_title", "Main page");
        return "about";
    }
}
