package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
//@RequestMapping
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
    @RequestMapping("/tiny")
    public String tinyMc(Model model, User user, Lesson lesson) {
        model.addAttribute("user", user);
        return "tiny";
    }

    @GetMapping("/lessons")
    public String listLessons(Model model) {
        List<Lesson> lessons = lessonService.lessonsListToAdminPage();
        model.addAttribute("lessons", lessons);
        return "lessons";
    }

    @RequestMapping("/lessons/lesson/{id}/edit")
    public String lessonsEdit(@PathVariable("id") Long id, Model model, Lesson lesson) {
        id = lesson.getId();
        lessonService.lessonSave(lesson);
        model.addAttribute("lesson", lesson);
        return "lesson-edit";
    }
    @GetMapping("/lessons/lesson/{id}")
    public String lessonEditPage(@PathVariable("id") Long id, Model model, Lesson lesson) {
        id = lesson.getId();
        lesson= lessonService.findById(id);
        model.addAttribute("lesson", lesson);
        return "lesson-edit";
    }
}
