package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.*;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Random;
import java.util.regex.Pattern;

@RestController
public class LessonController {
    private String ukrTextCheck;
    private String engTextCheck;
    private final LessonService lessonService;
    private final TranslationPairRandomFromLessonService translationPairRandomFromLessonService;

    private final TranslationPairValidationAndSaveService validationTranslationPair;
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;

    public LessonController(LessonService lessonService, TranslationPairRandomFromLessonService translationPairRandomFromLessonService,
                            UserStatisticsService userStatisticsService, TranslationPairValidationAndSaveService validationTranslationPair, UserService userService) {
        this.lessonService = lessonService;
        this.translationPairRandomFromLessonService = translationPairRandomFromLessonService;
        this.userStatisticsService = userStatisticsService;
        this.validationTranslationPair = validationTranslationPair;
        this.userService = userService;
    }

    @GetMapping(path = "/user/{userId}/lesson/{lessonId}/reload")
    public ResponseEntity<DtoTranslationPairToUI> randomTranslationPairToLesson(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
//                                                                                @RequestParam("lessonId") Long lessonIdRequest,
                                                                                Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            lessonId = lessonService.findById(lessonId).getId();
            userStatisticsService.repetitionsCountSave(userId);
            int generateNumber = new Random().nextInt(1, 4);
            if (generateNumber == 1){
                DtoTranslationPairToUI dtoTranslationPairToUI = translationPairRandomFromLessonService.translationPairRandom(lessonId, userId);
                ukrTextCheck = dtoTranslationPairToUI.getUkrText();
                engTextCheck = dtoTranslationPairToUI.getEngText();
                dtoTranslationPairToUI.setFragment("Content 1");
                return ResponseEntity.ok(dtoTranslationPairToUI);
            }else if(generateNumber == 2){
                DtoTranslationPairToUI dtoTranslationPairToUI = translationPairRandomFromLessonService.translationPairRandom(lessonId, userId);
                dtoTranslationPairToUI.setFragment("Content 2");
                ukrTextCheck = dtoTranslationPairToUI.getUkrText();
                engTextCheck = dtoTranslationPairToUI.getEngText();

                return ResponseEntity.ok(dtoTranslationPairToUI);
            }else{
                DtoTranslationPairToUI dtoTranslationPairToUI = translationPairRandomFromLessonService.translationPairRandom(lessonId, userId);
                dtoTranslationPairToUI.setFragment("Content 3");
//                ukrTextCheck = dtoTranslationPairToUI.getUkrText();
                return ResponseEntity.ok(dtoTranslationPairToUI);
            }
//            return ResponseEntity.ok(translationPairRandomFromLessonService.translationPairRandom(lessonId, userId));
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping(path = "/user/{userId}/lesson/{lessonId}/check")
    public ResponseEntity<String> textCheck(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
                                            @RequestParam("textCheck") String text, Principal principal, Model model){
        if(principal != null){
            if(text.equalsIgnoreCase(ukrTextCheck)){
                return ResponseEntity.ok("Чудово, гарна робота");
            } else if (text.equalsIgnoreCase(engTextCheck)) {
                return ResponseEntity.ok("Чудово, гарна робота");
            }else {
                if(Pattern.matches("[a-zA-Z]+", text)){
                    ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>" + engTextCheck + "</p>");
                } else return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>" + ukrTextCheck + "</p>");
            }
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
