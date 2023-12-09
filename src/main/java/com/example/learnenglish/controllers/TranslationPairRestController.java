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
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.TranslationPairService;
import com.example.learnenglish.service.PhraseUserService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TranslationPairRestController {
    private final UserService userService;
    private final HttpSession session;
    private final TranslationPairService translationPairService;
    private final PhraseUserService phraseUserService;


    @PostMapping("/translation-pair/check-edit")
    public ResponseEntity<?> checkEditTranslationPair(@Valid @RequestBody DtoTranslationPair dtoTranslationPair,
                                                      BindingResult bindingResult,
                                                      Principal principal) {
        if (principal != null) {
            if (bindingResult.hasErrors()) {
                List<FieldErrorDTO> errors = bindingResult.getFieldErrors().stream()
                        .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            String roleUser = userService.findByEmail(principal.getName()).getAuthority().toString();
            Optional<?> optional = phraseUserService.check(dtoTranslationPair, roleUser);
            Object object = optional.get();
            if(object instanceof DtoTranslationPairToUI){
                return ResponseEntity.ok((DtoTranslationPairToUI)object);
            } else return ResponseEntity.ok((CustomResponseMessage)object);
        }
        return ResponseEntity.notFound().build();
    }




}
