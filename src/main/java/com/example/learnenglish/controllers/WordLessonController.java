package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.*;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.CategoryService;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.WordInWordLessonService;
import com.example.learnenglish.service.WordLessonService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Data
public class WordLessonController {

    private final WordInWordLessonService wordInWordLessonService;

    private final WordLessonService wordLessonService;

    private final CategoryService categoryService;

    private final UserService userService;


    @GetMapping("/admin/word-lessons")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordLessonsListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                           Principal principal,
                                           Model model) {

        if (principal != null) {
            if (page < 0) page = 0;
            Page<WordLesson> wordLessonsPage = wordLessonService.getWordLessonsPage(page, size);
            if (wordLessonsPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", wordLessonsPage.getTotalPages());
            }
            model.addAttribute("wordLessons", wordLessonsPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/wordLessons";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/word-lessons/new-lesson")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String newWordLessonAdminPage(Principal principal) {
        if (principal != null) {
            try {
                long count = wordLessonService.countWordLesson() + 1;
                return "redirect:/admin/word-lessons/word-lesson-edit/" + count;
            } catch (NullPointerException e) {
                return "redirect:/admin/word-lessons/word-lesson-edit/1";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/word-lessons/word-lesson-edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordLessonEdit(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.LESSON_WORDS);
            model.addAttribute("category", "Відсутня");
            model.addAttribute("mainCategories", mainCategories);
            try {
                WordLesson wordLesson = wordLessonService.getWordLesson(id);
                if (wordLesson.getCategory() != null) {
                    model.addAttribute("category", wordLesson.getCategory().getName());
                }
                model.addAttribute("wordLesson", wordLesson);
            } catch (RuntimeException e) {
                model.addAttribute("wordLesson", wordLessonService.getNewWordLesson(id));
            }
            return "admin/wordLessonEdit";
        }
        return "redirect:/login";
    }

    @GetMapping("/word-lesson-categories")
    public String wordLessonsCategories(Model model, Principal principal) {
        if(principal != null) {
            List<Category> wordLessonMainCategory = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.LESSON_WORDS);
            List<Category> subcategories = new ArrayList<>();
            if(!wordLessonMainCategory.isEmpty()) {
                subcategories = wordLessonMainCategory.get(0).getSubcategories();
                for (int i = 0; i < subcategories.size(); i++) {
                    subcategories.get(i).setCountWordLessons(wordLessonService.countWordLessonInCategory(subcategories.get(i).getId()));
                }
                Collections.sort(subcategories, (obj1, obj2) -> obj1.getName().compareTo(obj2.getName()));
                model.addAttribute("subcategories", subcategories);
            }
            model.addAttribute("wordLessonMainCategory", wordLessonMainCategory);
            model.addAttribute("subcategories", subcategories);
            return "wordLessonCategories";
        } return "redirect:/login";
    }


    @GetMapping("/word-lesson-category/{id}/lessons")
    public String wordLessons(@PathVariable("id")Long categoryId,
                              Model model,
                              Principal principal) {
        if(principal != null) {
            User user = userService.findByEmail(principal.getName());
            Category category = categoryService.getCategory(categoryId);
            category.setCountWordLessons(wordLessonService.countWordLessonInCategory(category.getId()));
            List<WordLesson> wordLessons = wordLessonService.getWordLessonsCategory(user, categoryId);
            int sumWords = 0;
            for (WordLesson arr: wordLessons) {
                sumWords += arr.getWords().size();
            }
            model.addAttribute("category", category);
            model.addAttribute("wordLessons", wordLessons);
            model.addAttribute("words", sumWords);
            return "wordLessons";
        } return "redirect:/login";
    }


    @GetMapping("/word-lesson/{wordLessonId}")
    public String wordLesson(@PathVariable long wordLessonId, Model model, Principal principal) {
        if(principal != null) {
//            Page<WordInWordLesson> wordsFromLesson = wordInWordLessonService.wordsFromWordLesson(0, 2, wordLessonId);
//            if (wordsFromLesson.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", wordsFromLesson.getTotalPages());
//            }
//            model.addAttribute("words", wordsFromLesson.getContent());
            model.addAttribute("wordLessonId", wordLessonId);
            return "wordLesson";
        } return "redirect:/login";
    }





}
