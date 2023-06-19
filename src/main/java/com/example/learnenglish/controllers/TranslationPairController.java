package com.example.learnenglish.controllers;

import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.service.TranslationPairService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Controller
public class TranslationPairController {
    private final TranslationPairService translationPairService;

    public TranslationPairController(TranslationPairService translationPairService) {
        this.translationPairService = translationPairService;
    }
    @GetMapping("/user/{id}/translation-pairs")
    public String translationPairsListAdminPage(Model model,
                                                Principal principal,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                @PathVariable("id") Long userId) {
        if (principal != null) {
            if(page < 0) page = 0;
            Page<TranslationPair> translationPairsPage = translationPairService.getTranslationPairsPage(page, size, userId);
            if(translationPairsPage.getTotalPages() == 0){
                model.addAttribute("totalPages", 1);
            }else{
                model.addAttribute("totalPages", translationPairsPage.getTotalPages());
            }
            model.addAttribute("translationPairs", translationPairsPage.getContent());
            model.addAttribute("currentPage", page);
            return "userTranslationPairs";
        }
        return "redirect:/login";
    }
    @PostMapping("/trsnslation-pair/check-edit")
    public void checkEditTranslationPair(@RequestBody TranslationPair translationPair){

    }
}
