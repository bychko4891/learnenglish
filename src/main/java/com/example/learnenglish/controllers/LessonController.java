package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@RestController
public class LessonController {
    private final LessonService lessonService;
    private final HttpSession session;
    private final TranslationPairRandomFromLessonService translationPairRandomFromLessonService;

    private final TranslationPairValidationAndSaveService validationTranslationPair;
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;

    public LessonController(LessonService lessonService, HttpSession session, TranslationPairRandomFromLessonService translationPairRandomFromLessonService,
                            UserStatisticsService userStatisticsService, TranslationPairValidationAndSaveService validationTranslationPair, UserService userService) {
        this.lessonService = lessonService;
        this.session = session;
        this.translationPairRandomFromLessonService = translationPairRandomFromLessonService;
        this.userStatisticsService = userStatisticsService;
        this.validationTranslationPair = validationTranslationPair;
        this.userService = userService;
    }

    @GetMapping(path = "/user/{userId}/lesson/{lessonId}/reload")
    public ResponseEntity<DtoTranslationPairToUI> randomTranslationPairToLesson(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
//                                                                                @RequestParam("lessonId") Long lessonIdRequest,
                                                                                Principal principal, Model model) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            lessonId = lessonService.findById(lessonId).getId();
            userStatisticsService.repetitionsCountSave(userId);
            int generateNumber = new Random().nextInt(1, 5);
            DtoTranslationPairToUI dtoTranslationPairToUI = translationPairRandomFromLessonService.translationPairRandom(lessonId, userId);
            session.setAttribute("ukrTextCheck", dtoTranslationPairToUI.getUkrText());
            session.setAttribute("engTextCheck", dtoTranslationPairToUI.getEngText());
            dtoTranslationPairToUI.setFragment("Fragment " + generateNumber);
            if (generateNumber == 4) {
               dtoTranslationPairToUI.setEngTextList(Arrays.asList(dtoTranslationPairToUI.getEngText().replaceAll("\\?+", "").split(" ")));
            }
            return ResponseEntity.ok(dtoTranslationPairToUI);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/user/{userId}/lesson/{lessonId}/check")
    public ResponseEntity<String> textCheck(@PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
                                            @RequestParam("textCheck") String text, Principal principal, Model model) {
        if (principal != null) {

            if (text.equalsIgnoreCase((String)session.getAttribute("ukrTextCheck"))) {
                return ResponseEntity.ok("Чудово, гарна робота");
            } else if (text.equalsIgnoreCase((String)session.getAttribute("engTextCheck"))) {
                return ResponseEntity.ok("Чудово, гарна робота");
            } else {
                if (Pattern.matches("[a-zA-Z]+", text)) {
                    ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
                            + session.getAttribute("ukrTextCheck") + "</p>");
                } else
                    return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
                            + session.getAttribute("engTextCheck") + "</p>");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/text-chec")
    public ResponseEntity<String> textCheckDrop(
//            @PathVariable(value = "userId") long userId, @PathVariable(value = "lessonId") long lessonId,
                                            @RequestParam("textCheck") String text, Principal principal) {
        if (principal != null) {

            if (text.equalsIgnoreCase((String)session.getAttribute("engTextCheck"))) {
                return ResponseEntity.ok("Чудово, гарна робота");
            } else {
                if (Pattern.matches("[a-zA-Z]+", text)) {
                    ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
                            + session.getAttribute("ukrTextCheck") + "</p>");
                } else
                    return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
                            + session.getAttribute("engTextCheck") + "</p>");
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
