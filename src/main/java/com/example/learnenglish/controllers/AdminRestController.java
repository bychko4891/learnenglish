package com.example.learnenglish.controllers;
/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */
import com.example.learnenglish.dto.DtoTextOfAppPage;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.TextOfAppPageService;
import com.example.learnenglish.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/text-of-app-page/{id}/edit")
    public ResponseEntity<ResponseMessage> createAppTextPage(@RequestBody DtoTextOfAppPage dtoTextOfAppPage,
                                                             Principal principal) {
        if (principal != null) {
//            textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage);
            return ResponseEntity.ok(textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/lessons/lesson/{id}/edit")
    public ResponseEntity<String> lessonsEdit(@PathVariable("id") Long id,
                                              @RequestBody Lesson lesson,
                                              Principal principal) {
//        model.addAttribute("lesson", lesson);
        if (principal != null) {
            id = lesson.getId();
            lessonService.lessonEdit(lesson);
            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }


    @PostMapping("/users/user/user-active")
    @ResponseBody
    public void userFieldActive(@RequestParam("userActive") boolean userActive,
                                @RequestParam("userId") Long userId,
                                Principal principal) {
        if (principal != null) {
            userService.userActiveEdit(userId, userActive);
            System.out.println(userActive);
//                return ResponseEntity.ok(new ResponseStatus(Message.SUCCESS_CREATELESSON));
        }
//        return ResponseEntity.ok(new ResponseStatus(Message.ERROR_CREATELESSON));
    }
}
