package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.LessonService;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final LessonService lessonService;

    public AdminController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    //    @RequestMapping(path = {"/","/user/{user}"})
//    public String userInfo(@PathVariable("user") User user, Model model) {
//        model.addAttribute("user", user);
//        return "user-info";
//    }

    @GetMapping("/lessons")
    public String listLessons(Model model, Principal principal) {
        if (principal != null) {
            List<Lesson> lessons = lessonService.lessonsListToAdminPage();
            model.addAttribute("lessons", lessons);
            return "lessons";
        }
        return "redirect:/login";
    }

    @RequestMapping("/lessons/lesson/{id}/edit")
    public String lessonsEdit(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {
        if (principal != null) {
            id = lesson.getId();
            lessonService.lessonSave(lesson);
            model.addAttribute("lesson", lesson);
            return "lesson-edit";
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons/lesson/{id}")
    public String lessonEditPage(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {
        if (principal != null) {
            id = lesson.getId();
            lesson = lessonService.findById(id);
            if (lesson.getLessonInfo() != null) {
                model.addAttribute("lesson", lesson);
                return "lesson-edit";
            } else {
                lesson.setName("Заняття № " + lesson.getId());
                lesson.setLessonInfo("Опис заняття");
                model.addAttribute("lesson", lesson);
                return "lesson-edit";
            }
        }
        return "redirect:/login";
    }
}