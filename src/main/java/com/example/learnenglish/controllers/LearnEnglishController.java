package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.MiniStory;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LearnEnglishController {

    private final PageApplicationService pageApplicationService;
    private final CategoryService categoryService;
    private final WordService wordService;
    private final MiniStoryService miniStoryService;


    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            return "redirect:/lesson/1";
        } else {
            PageApplication mainTop = pageApplicationService.getPageApplication(5l);
            PageApplication mainBottom = pageApplicationService.getPageApplication(6l);
            if (mainTop.getTextOfAppPage() != null) {
                model.addAttribute("mainTop", mainTop.getTextOfAppPage().getText());
            } else {
                model.addAttribute("mainTop", "No text in this page");
            }
            if (mainBottom.getTextOfAppPage() != null) {
                model.addAttribute("mainBottom", mainBottom.getTextOfAppPage().getText());
            }
            model.addAttribute("title", "Англійська за 16 годин - e-learn.top");
            return "main";
        }
    }

    @GetMapping("/test")
    public String indexTest(Model model) {
        PageApplication mainTop = pageApplicationService.getPageApplication(5l);
        PageApplication mainBottom = pageApplicationService.getPageApplication(6l);
        if (mainTop.getTextOfAppPage() != null) {
            model.addAttribute("mainTop", mainTop.getTextOfAppPage().getText());
        } else {
            model.addAttribute("mainTop", "No text in this page");
        }
        if (mainBottom.getTextOfAppPage() != null) {
            model.addAttribute("mainBottom", mainBottom.getTextOfAppPage().getText());
        }
        model.addAttribute("title", "Англійська за 16 годин - e-learn.top");
            return "temp";
    }


    @GetMapping("/about-the-app")
    public String aboutApp(Model model) {
        model.addAttribute("title", "About the app Learn English");
        PageApplication pageApplication = pageApplicationService.getPageApplication(3l);
        if (pageApplication.getTextOfAppPage() != null) {
            model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
        } else {
            model.addAttribute("pageText", "No text in this page");
        }
        return "about-the-app";
    }
    @GetMapping("/lessons")
    public String lessons(Model model) {
        model.addAttribute("title", "About the app Learn English");
        PageApplication pageApplication = pageApplicationService.getPageApplication(3l);
        if (pageApplication.getTextOfAppPage() != null) {
            model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
        } else {
            model.addAttribute("pageText", "No text in this page");
        }
        return "lessons";
    }


    @GetMapping("/phrases-categories")
    public String phrasesMainCategories(Model model) {
        List<Category> phrasesMainCategories = categoryService.mainTranslationPairsCategoryListUser(true);
        if (phrasesMainCategories != null) {
            model.addAttribute("phrasesMainCategories", phrasesMainCategories);
        }
        return "miniStoriesMainCategories";
    }

    @GetMapping("/phrases-page/{id}")
    public String getTranslationPairsPage(@PathVariable("id")Long id,
                                          Model model) {
        MiniStory miniStory = miniStoryService.getTranslationPairsPage(id);
        model.addAttribute("translationPairsPage", miniStory);
        if (miniStory.getCategory() != null) {
            model.addAttribute("category", miniStory.getCategory());
        }
        return "phrasesPage";
    }

    @GetMapping("/phrases-category/{id}/phrases-pages")
    public String translationPairsPages(@PathVariable("id") Long id,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                        Model model) {
            if (page < 0) page = 0;
            Page<MiniStory> translationPairsPages = miniStoryService.getTranslationPairsPagesToUser(page, size, id);
            Category category = categoryService.getCategory(id);
            if (translationPairsPages.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", translationPairsPages.getTotalPages());
            }
            model.addAttribute("translationPairsPages", translationPairsPages.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("categoryId", id);
            model.addAttribute("categoryName", category.getName());

            return "phrasesPages";

    }





    @GetMapping("/word/{id}")
    public String word(@PathVariable Long id, Model model) {
        Word word = wordService.getWord(id);
//        Category wordCategory = word.getCategory().getParentCategory();
        model.addAttribute("word", word);
//        if(wordCategory != null){
//            model.addAttribute("mainCategoryId", wordCategory.getParentCategory().getId());
//            model.addAttribute("mainCategoryName", wordCategory.getName());
//        }
        return "word";
    }


}
