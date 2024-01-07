package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.PhraseLesson;
import com.example.learnenglish.service.CategoryService;
import com.example.learnenglish.service.PageApplicationService;
import com.example.learnenglish.service.PhraseLessonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class PhraseLessonController {

    private final PhraseLessonService phraseLessonService;

    private final CategoryService categoryService;

    private final PageApplicationService pageApplicationService;

    @GetMapping("/phrase-lessons/categories")
    public String lessonPage(PhraseLesson phraseLesson, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
//        if (principal != null) {
//            List<Category> subcategoryPhraseLesson = categoryService.getSubcategoriesPhraseLesson();
//
//            PageApplication pageApplication = pageApplicationService.getPageApplication(4l);
//            if (pageApplication.getTextOfAppPage() != null) {
//                model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
//            } else {
//                model.addAttribute("pageText", "No text in this page");
//            }
//            model.addAttribute("categories", subcategoryPhraseLesson);
//            return "subcategoriesPhraseLesson";
//        }
        return "redirect:/login";
    }
}
