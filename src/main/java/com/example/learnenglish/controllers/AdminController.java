package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final LessonService lessonService;
    private final UserService userService;

    public AdminController(LessonService lessonService, UserService userService) {
        this.lessonService = lessonService;
        this.userService = userService;
    }

    //    @RequestMapping(path = {"/","/user/{user}"})
//    public String userInfo(@PathVariable("user") User user, Model model) {
//        model.addAttribute("user", user);
//        return "user-info";
//    }
//    @GetMapping("/*")
//    public void useraddPage(Model model, Principal principal){
//        if (principal != null) {
//            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("avatarName",user.getUserAvatar().getAvatarName());
//            model.addAttribute("user", user);
//        }
//    }
    @GetMapping
    public String adminPage(Model model, Principal principal) {
        if (principal != null) {

            User user = userService.findByEmail(principal.getName());
//            List<Lesson> lessons = lessonService.lessonsListToAdminPage();
            model.addAttribute("user", user);
//            model.addAttribute("lessons", lessons);
            return "admin-page";
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons")
    public String listLessons(Model model, Principal principal) {
        if (principal != null) {
            List<Lesson> lessons = lessonService.lessonsListToAdminPage();
            model.addAttribute("lessons", lessons);
            return "lessons";
        }
        return "redirect:/login";
    }



    @GetMapping("/lessons/lesson/{id}")
    public String lessonEditPage(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {
        if (principal != null) {
            id = lesson.getId();
            lesson = lessonService.findById(id);
            if (lesson.getLessonInfo() == null || lesson.getName().equals("newLesson")) {
                lesson.setName("Заняття № " + lesson.getId());
                lesson.setLessonInfo("Опис заняття");
                model.addAttribute("lesson", lesson);
                return "lesson-edit";
            } else {
                model.addAttribute("lesson", lesson);
                return "lesson-edit";
            }
        }
        return "redirect:/login";
    }
}