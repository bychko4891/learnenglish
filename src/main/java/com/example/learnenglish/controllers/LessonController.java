package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.TranslationPairRandomFromLessonService;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import com.example.learnenglish.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

@RestController
public class LessonController {
    private final TranslationPairRandomFromLessonService translationPairRandomFromLessonService;
    private final TranslationPairValidationAndSaveService validationTranslationPair;
    private final UserService userService;

    public LessonController(TranslationPairRandomFromLessonService translationPairRandomFromLessonService, TranslationPairValidationAndSaveService validationTranslationPair, UserService userService) {
        this.translationPairRandomFromLessonService = translationPairRandomFromLessonService;
        this.validationTranslationPair = validationTranslationPair;
        this.userService = userService;
    }

@GetMapping(path = "/user/{userId}/lesson/{lessonId}/reload")
public void randomTranslationPairToLesson(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
                                          HttpServletResponse response, HttpServletRequest request, Principal principal) {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    lessonId = Long.parseLong(request.getParameter("lessonId"));
    PrintWriter printWriter = null;
    if (principal != null) {
        userId = userService.findByEmail(principal.getName()).getId();
        try {
            DtoTranslationPairToUI dtoTranslationPairToUI = translationPairRandomFromLessonService.translationPairRandom(lessonId, userId);
            printWriter = response.getWriter();
            printWriter.println(new ObjectMapper().writeValueAsString(dtoTranslationPairToUI));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            printWriter.close();
        }
    }
}
    @PostMapping(path = "/user/{userId}/lesson/{lessonId}/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseStatus> textADD(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
                                                  @Validated @RequestBody DtoTranslationPair dtoTranslationPair, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            lessonId = dtoTranslationPair.getLessonId();
            return ResponseEntity.ok(validationTranslationPair.validationTranslationPair(dtoTranslationPair));
        }
        return ResponseEntity.ok(new ResponseStatus(Message.ERRORLOGIN));
    }

}
