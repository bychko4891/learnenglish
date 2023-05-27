package com.example.learnenglish.controllers;

import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.TextOfAppPageService;
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
    private final TextOfAppPageService textOfAppPageService;

    public AdminRestController(LessonService lessonService, UserService userService, TextOfAppPageService textOfAppPageService) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.textOfAppPageService = textOfAppPageService;
    }

    @PostMapping("/text-of-app-page/{id}/create-app-text-page")
    public ResponseEntity<String> createAppTextPage(@RequestBody TextOfAppPage textOfAppPage, Model model,
                                                    Principal principal) {
        if (principal != null) {
            textOfAppPageService.saveTextOfApppage(textOfAppPage);
            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }

    @PostMapping("/lessons/lesson/{id}/edit")
    public ResponseEntity<String> lessonsEdit(@PathVariable("id") Long id, Model model,
                                              @RequestBody Lesson lesson, Principal principal) {

//        model.addAttribute("lesson", lesson);
        if (principal != null) {
            id = lesson.getId();
            lessonService.lessonEdit(lesson);
            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }

    @PostMapping("/lessons/add-new-lesson")
    public ResponseEntity<ResponseStatus> createLessons(@RequestBody Lesson lesson, Principal principal) {
        if (principal != null) {
//            lessonService.createLesson(lesson);
            if (lessonService.createLesson(lesson))
                return ResponseEntity.ok(new ResponseStatus(Message.SUCCESS_CREATELESSON));
        }
        return ResponseEntity.ok(new ResponseStatus(Message.ERROR_CREATELESSON));
    }

    @PostMapping("/users/user/user-active")
    @ResponseBody
    public void userFieldActive(@RequestParam("userActive") boolean userActive,
                                @RequestParam("userId") Long userId, Principal principal) {
        if (principal != null) {
            userService.userActiveEdit(userId, userActive);
            System.out.println(userActive);
//                return ResponseEntity.ok(new ResponseStatus(Message.SUCCESS_CREATELESSON));
        }
//        return ResponseEntity.ok(new ResponseStatus(Message.ERROR_CREATELESSON));
    }
}
