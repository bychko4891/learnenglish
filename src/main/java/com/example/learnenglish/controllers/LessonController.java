package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.TranslationPairService;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.UserStatisticsService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
public class LessonController {
    private final HttpSession session;
    private final TranslationPairService translationPairService;

    private final TranslationPairValidationAndSaveService validationTranslationPair;
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;

    public LessonController(HttpSession session, TranslationPairService translationPairService,
                            UserStatisticsService userStatisticsService, TranslationPairValidationAndSaveService validationTranslationPair,
                            UserService userService) {
        this.session = session;
        this.translationPairService = translationPairService;
        this.userStatisticsService = userStatisticsService;
        this.validationTranslationPair = validationTranslationPair;
        this.userService = userService;
    }

    @GetMapping(path = "/lesson/{lessonId}/reload")
    public ResponseEntity<DtoTranslationPairToUI> randomTranslationPairToLesson(@PathVariable(value = "lessonId") long lessonId,
//                                                                                @RequestParam("lessonId") Long lessonIdRequest,
                                                                                Principal principal) {
        if (principal != null) {
            long userId = (long)session.getAttribute("userId");
            userStatisticsService.repetitionsCountSave(userId);
            DtoTranslationPairToUI dtoTranslationPairToUI = translationPairService.translationPairRandom(lessonId, userId);
            session.setAttribute("ukrText", dtoTranslationPairToUI.getUkrText());
            session.setAttribute("engText", dtoTranslationPairToUI.getEngText());
            session.setAttribute("fragment", dtoTranslationPairToUI.getFragment());
            session.setAttribute("engTextCheck", dtoTranslationPairToUI.getEngText().replaceAll("\\?+", ""));
            session.setAttribute("ukrTextCheck", dtoTranslationPairToUI.getUkrText().replaceAll("\\?+", ""));
            return ResponseEntity.ok(dtoTranslationPairToUI);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/lesson/{lessonId}/check")
    public ResponseEntity<String> textCheck(@PathVariable(value = "lessonId") long lessonId,
                                            @RequestParam("textCheck") String text, Principal principal) {
        if (principal != null) {
            String textCheck = StringUtils.normalizeSpace(text);
            textCheck = textCheck.replaceAll("[.,!~?$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
            if (textCheck.equalsIgnoreCase((String) session.getAttribute("ukrTextCheck")) || textCheck.equalsIgnoreCase((String) session.getAttribute("engTextCheck"))) {
                return ResponseEntity.ok("Чудово, гарна робота");
            } else {
                if (session.getAttribute("fragment").equals("Fragment 1") || session.getAttribute("fragment").equals("Fragment 4")) {
                    return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
                            + session.getAttribute("engText") + "</p>");
                } else
                    return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
                            + session.getAttribute("ukrText") + "</p>");
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping(path = "/translation-pair/add")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseStatus> textADD(@Validated @RequestBody DtoTranslationPair dtoTranslationPair, Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(validationTranslationPair.validationTranslationPair(dtoTranslationPair));
        }
        return ResponseEntity.ok(new ResponseStatus(Message.ERRORLOGIN));
    }

}
