package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.dto.FieldErrorDTO;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.TranslationPairService;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TranslationPairRestController {
    private final UserService userService;
    private final HttpSession session;
    private final TranslationPairService translationPairService;
    private final TranslationPairValidationAndSaveService translationPairValidationAndSaveService;

    public TranslationPairRestController(UserService userService,
                                         HttpSession session,
                                         TranslationPairService translationPairService,
                                         TranslationPairValidationAndSaveService translationPairValidationAndSaveService) {
        this.userService = userService;
        this.session = session;
        this.translationPairService = translationPairService;
        this.translationPairValidationAndSaveService = translationPairValidationAndSaveService;
    }

    @PostMapping("/translation-pair/check-edit")
    public ResponseEntity<?> checkEditTranslationPair(@Valid @RequestBody DtoTranslationPair dtoTranslationPair,
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
            Optional<?> optional = translationPairValidationAndSaveService.check(dtoTranslationPair, roleUser);
            Object object = optional.get();
            if(object instanceof DtoTranslationPairToUI){
                return ResponseEntity.ok((DtoTranslationPairToUI)object);
            } else return ResponseEntity.ok((ResponseMessage)object);
//            DtoTranslationPairToUI dtoTranslationPairToUI = translationPairValidationAndSaveService.check(dtoTranslationPair, roleUser);

        }
        return ResponseEntity.notFound().build();
    }
//    @PostMapping("/translation-pair/check-edit")
//    public ResponseEntity<DtoTranslationPairToUI> checkEditTranslationPair(@RequestBody TranslationPair translationPair){
//        DtoTranslationPairToUI dtoTranslationPairToUI = translationPairValidationAndSaveService.check(translationPair);
//        return ResponseEntity.ok(dtoTranslationPairToUI);
//    }
}
