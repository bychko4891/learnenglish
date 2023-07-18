package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.dto.FieldErrorDTO;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.TranslationPairService;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.UserStatisticsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class LessonController {
    private final HttpSession session;
    private final TranslationPairService translationPairService;

    private final TranslationPairValidationAndSaveService validationTranslationPair;
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;

    public LessonController(HttpSession session,
                            TranslationPairService translationPairService,
                            UserStatisticsService userStatisticsService,
                            TranslationPairValidationAndSaveService validationTranslationPair,
                            UserService userService) {
        this.session = session;
        this.translationPairService = translationPairService;
        this.userStatisticsService = userStatisticsService;
        this.validationTranslationPair = validationTranslationPair;
        this.userService = userService;
    }

    @GetMapping(path = "/lesson/{lessonId}/reload")
    public ResponseEntity<DtoTranslationPairToUI> randomTranslationPairToLesson(@PathVariable(value = "lessonId") long lessonId,
                                                                                Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            String userGender = (String) session.getAttribute("userGender");
            DtoTranslationPairToUI dtoTranslationPairToUI = translationPairService.getDtoTranslationPair(user, lessonId, userGender);
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
                                            @RequestParam("textCheck") String text,
                                            Principal principal) {
        if (principal != null) {
            String textCheck = StringUtils.normalizeSpace(text);
            textCheck = textCheck.replaceAll("[.,!~?$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
            if (textCheck.equalsIgnoreCase((String) session.getAttribute("ukrTextCheck")) || textCheck.equalsIgnoreCase((String) session.getAttribute("engTextCheck"))) {
                return ResponseEntity.ok("Чудово, гарна робота");
            } else {
                userStatisticsService.errorUserRepetitionCount(userService.findByEmail(principal.getName()).getId());
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
    public ResponseEntity<?> textADD(@Valid @RequestBody DtoTranslationPair dtoTranslationPair,
                                     BindingResult bindingResult,
                                     Principal principal) {
        if (principal != null) {
            if (bindingResult.hasErrors()) {
                // Опрацювання помилок валідації

                List<FieldErrorDTO> errors = bindingResult.getFieldErrors().stream()
                        .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            String roleUser = userService.findByEmail(principal.getName()).getAuthority().toString();
            System.out.println(roleUser);
            return ResponseEntity.ok(validationTranslationPair.saveTranslationPair(dtoTranslationPair, roleUser));
        }
        return ResponseEntity.ok(new ResponseMessage(Message.ERRORLOGIN));
    }

}
