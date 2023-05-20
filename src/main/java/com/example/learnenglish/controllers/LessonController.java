package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.TranslationPairRandomFromLessonService;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.UserStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class LessonController {
    private final TranslationPairRandomFromLessonService translationPairRandomFromLessonService;

    private final TranslationPairValidationAndSaveService validationTranslationPair;
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;

    public LessonController(TranslationPairRandomFromLessonService translationPairRandomFromLessonService, UserStatisticsService userStatisticsService, TranslationPairValidationAndSaveService validationTranslationPair, UserService userService) {
        this.translationPairRandomFromLessonService = translationPairRandomFromLessonService;
        this.userStatisticsService = userStatisticsService;
        this.validationTranslationPair = validationTranslationPair;
        this.userService = userService;
    }

    @GetMapping(path = "/user/{userId}/lesson/{lessonId}/reload")
    public ResponseEntity<DtoTranslationPairToUI> randomTranslationPairToLesson(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
                                                                                @RequestParam("lessonId") Long lessonIdRequest, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            lessonId = lessonIdRequest;
            userStatisticsService.repetitionsCountSave(userId);
            return ResponseEntity.ok(translationPairRandomFromLessonService.translationPairRandom(lessonId, userId));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping(path = "/user/{userId}/lesson/{lessonId}/add")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
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
