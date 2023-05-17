package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/lessons/lesson/{id}/edit")
    public ResponseEntity<String> lessonsEdit(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {

//        model.addAttribute("lesson", lesson);
        if (principal != null) {
            id = lesson.getId();
            lessonService.lessonEdit(lesson);
            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }
    @PostMapping("/lessons/add-new-lesson")
    public ResponseEntity<String> createLessons(@RequestBody Lesson lesson, Principal principal) {
        if (principal != null) {
//            lessonService.createLesson(lesson);
            if(lessonService.createLesson(lesson)) return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }
}
