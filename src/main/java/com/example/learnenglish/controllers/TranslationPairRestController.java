package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.service.TranslationPairService;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslationPairRestController {
    private final TranslationPairService translationPairService;
    private final TranslationPairValidationAndSaveService translationPairValidationAndSaveService;

    public TranslationPairRestController(TranslationPairService translationPairService, TranslationPairValidationAndSaveService translationPairValidationAndSaveService) {
        this.translationPairService = translationPairService;
        this.translationPairValidationAndSaveService = translationPairValidationAndSaveService;
    }

    @PostMapping("/translation-pair/check-edit")
    public ResponseEntity<DtoTranslationPairToUI> checkEditTranslationPair(@RequestBody TranslationPair translationPair){
        DtoTranslationPairToUI dtoTranslationPairToUI = translationPairValidationAndSaveService.check(translationPair);
        return ResponseEntity.ok(dtoTranslationPairToUI);
    }
}
