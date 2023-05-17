package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
@RestController
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminRestController {
    private final LessonService lessonService;
    private final UserService userService;
    public AdminRestController(LessonService lessonService, UserService userService) {
        this.lessonService = lessonService;
        this.userService = userService;
    }

    @RequestMapping("/lessons/lesson/{id}/edit")
    public ResponseEntity<String> lessonsEdit(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {

//        model.addAttribute("lesson", lesson);
        if (principal != null) {
            id = lesson.getId();
            lessonService.lessonSave(lesson);

            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }
}
