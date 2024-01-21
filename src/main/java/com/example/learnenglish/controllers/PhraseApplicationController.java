package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.PhraseApplication;
import com.example.learnenglish.service.PhraseApplicationService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Data
public class PhraseApplicationController {

    private final PhraseApplicationService phraseApplicationService;


    @GetMapping("/admin/phrases-application")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String translationPairsListAdminPage(Model model,
                                                Principal principal,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<PhraseApplication> phraseApplicationPage = phraseApplicationService.getAllPhraseApplication(page, size);
            model.addAttribute("phrasesApplication", phraseApplicationPage.getContent());
            model.addAttribute("currentPage", page);
            if (phraseApplicationPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", phraseApplicationPage.getTotalPages());
            }
            return "admin/phrasesApplication";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/phrases-application/new-phrase-application")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String newPhraseApplication(Principal principal) {
        if (principal != null) {
            try {
                long count = phraseApplicationService.countPhraseApplication() + 1;
                return "redirect:/admin/phrases-application/phrase-application-edit/" + count;
            } catch (NullPointerException e) {
                return "redirect:/admin/phrases-application/phrase-application-edit/1";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/phrases-application/phrase-application-edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String phraseApplicationEdit(@PathVariable("id") long phraseApplicationId, Model model, Principal principal) {
        if (principal != null) {
            try {
                PhraseApplication pa = phraseApplicationService.getPhraseApplication(phraseApplicationId);
                model.addAttribute("phrase", pa);
            } catch (ObjectNotFoundException e) {
                model.addAttribute("phrase", phraseApplicationService.newPhraseApplication(phraseApplicationId));
            }
            return "admin/phraseApplicationEdit";
        }
        return "redirect:/login";
    }

}
