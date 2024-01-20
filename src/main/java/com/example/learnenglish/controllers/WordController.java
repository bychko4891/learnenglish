package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Word;
import com.example.learnenglish.service.WordService;
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
public class WordController {

    private final WordService wordService;

    @GetMapping("/admin/words")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordsListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "30", required = false) int size,
                                     Principal principal,
                                     Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Word> wordPage = wordService.getWordsPage(page, size);
            if (wordPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", wordPage.getTotalPages());
            }
            model.addAttribute("words", wordPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/words";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/words/new-word")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String newWordAdminPage(Principal principal) {
        if (principal != null) {
            try {
                long count = wordService.countWords() + 1;
                return "redirect:/admin/words/word-edit/" + count;
            } catch (NullPointerException e) {
                return "redirect:/admin/words/word-edit/1";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/words/word-edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordEdit(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal != null) {
            try {
                Word word = wordService.getWord(id);
                model.addAttribute("word", word);
            } catch (RuntimeException e) {
                model.addAttribute("word", wordService.getNewWord(id));
            }
            return "admin/wordEdit";
        }
        return "redirect:/login";
    }
}
