package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.model.VocabularyPage;
import com.example.learnenglish.service.CategoryService;
import com.example.learnenglish.service.VocabularyPageService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@Data
public class VocabularyPageController {

    private final VocabularyPageService vocabularyPageService;
    private final CategoryService categoryService;

    @GetMapping("/admin/vocabulary-pages")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordsListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "30", required = false) int size,
                                     Principal principal,
                                     Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<VocabularyPage> vocabularyPages = vocabularyPageService.getVocabularyPages(page, size);
            if (vocabularyPages.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", vocabularyPages.getTotalPages());
            }
            model.addAttribute("vocabularyPages", vocabularyPages.getContent());
            model.addAttribute("currentPage", page);

            return "admin/vocabularyPages";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/vocabulary-pages/new-vocabulary-page")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String newVocabularyPage(Principal principal) {
        if (principal != null) {
            try {
                long count = vocabularyPageService.countVocabularyPages() + 1;
                return "redirect:/admin/vocabulary-pages/vocabulary-page-edit/" + count;
            } catch (RuntimeException e) {
                return "redirect:/admin/vocabulary-pages/vocabulary-page-edit/1";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/vocabulary-pages/vocabulary-page-edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editVocabularyPage(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.mainCategoryListByCategoryPage(true, CategoryPage.VOCABULARY_PAGE);
            model.addAttribute("category", "Відсутня");
            model.addAttribute("mainCategories", mainCategories);
            try {
                VocabularyPage vocabularyPage = vocabularyPageService.getVocabularyPage(id);
                if (vocabularyPage.getCategory() != null) {
                    model.addAttribute("category", vocabularyPage.getCategory().getName());
                }
                model.addAttribute("vocabularyPage", vocabularyPage);
            } catch (RuntimeException e) {
                model.addAttribute("vocabularyPage", vocabularyPageService.getNewVocabularyPage(id));
            }
            return "admin/vocabularyPageEdit";
        }
        return "redirect:/login";
    }

}
