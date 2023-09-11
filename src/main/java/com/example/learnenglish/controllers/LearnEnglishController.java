package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.*;
import com.example.learnenglish.model.users.User;
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
    private final HttpSession session;
    private final UserService userService;
    private final LessonService lessonService;
    private final PageApplicationService pageApplicationService;
    private final CategoryService categoryService;
    private final WordService wordService;
    private final WordLessonService wordLessonService;
    private final TranslationPairPageService translationPairPageService;


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
            return "index";
        }
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


    @GetMapping("/lesson/{lessonId}")
    public String lessonPage(@PathVariable("lessonId") Long lessonId,
                             Lesson lesson, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            PageApplication pageApplication = pageApplicationService.getPageApplication(4l);
            if (pageApplication.getTextOfAppPage() != null) {
                model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
            } else {
                model.addAttribute("pageText", "No text in this page");
            }
            lesson = lessonService.getLesson(lessonId);
            session.setAttribute("lessonId", lesson.getId());
            model.addAttribute("lessonId", lesson.getId());
            model.addAttribute("lesson", lesson);
            return "translationPairLesson";
        }
        return "redirect:/login";
    }

    @GetMapping("/words-main-category")
    public String wordsMainCategories(Model model) {
        List<Category> wordsMainCategories = categoryService.mainWordCategoryList(true);
        if (wordsMainCategories != null) {
            model.addAttribute("wordsMainCategories", wordsMainCategories);
        }
        return "wordsMainCategory";
    }

    @GetMapping("/phrases-categories")
    public String phrasesMainCategories(Model model) {
        List<Category> phrasesMainCategories = categoryService.mainTranslationPairsCategoryListUser(true);
        if (phrasesMainCategories != null) {
            model.addAttribute("phrasesMainCategories", phrasesMainCategories);
        }
        return "phrasesCategories";
    }

    @GetMapping("/phrases-page/{id}")
    public String getTranslationPairsPage(@PathVariable("id")Long id,
                                          Model model) {
        TranslationPairsPage translationPairsPage = translationPairPageService.getTranslationPairsPage(id);
        model.addAttribute("translationPairsPage", translationPairsPage);
        if (translationPairsPage.getTranslationPairsPageCategory() != null) {
            model.addAttribute("category", translationPairsPage.getTranslationPairsPageCategory());
        }
        return "phrasesPage";
    }

    @GetMapping("/phrases-category/{id}/phrases-pages")
    public String translationPairsPages(@PathVariable("id") Long id,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                        Model model) {
            if (page < 0) page = 0;
            Page<TranslationPairsPage> translationPairsPages = translationPairPageService.getTranslationPairsPagesToUser(page, size, id);
            Category category = categoryService.getCategoryToEditor(id);
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

    @GetMapping("/words-main-category/{id}")
    public String wordsSubcategoriesFromMainCategories(@PathVariable Long id, Model model) {
        Category mainWordsCategory = categoryService.getCategoryToEditor(id);

        if (mainWordsCategory.isViewSubcategoryFullNoInfoOrNameAndInfo()) {
            return "wordsSubcategoryNameAndInfo";
        } else {
            List<Category> wordsSubCategoriesAndSubSubInMainCategory = categoryService.getSubcategoriesAndSubSubcategoriesInMainCategory(id);
            model.addAttribute("wordsSubCategories", wordsSubCategoriesAndSubSubInMainCategory);
            model.addAttribute("mainCategoryId", mainWordsCategory.getId());
            return "wordsSubcategoryFullAndNoInfo";
        }
    }

    @GetMapping("/subcategory/{id}")
    public String wordsSubcategories(@PathVariable Long id, Model model) {
        Category subcategory = categoryService.getCategoryToEditor(id);
        Category parentCategory = subcategory.getParentCategory();
        model.addAttribute("words", subcategory.getWords());
        model.addAttribute("subId", subcategory.getId());
        model.addAttribute("mainId", parentCategory.getParentCategory().getId());
        return "wordsSubcategoryView";
    }
    @GetMapping("/word-lessons/{id}")
    public String word(@PathVariable Long id, Model model) {
        Word word = wordService.getWord(id);
        Category wordCategory = word.getWordCategory().getParentCategory();
        model.addAttribute("word", word);
        if(wordCategory != null){
            model.addAttribute("mainCategoryId", wordCategory.getParentCategory().getId());
            model.addAttribute("mainCategoryName", wordCategory.getName());
        }
        return "word";
    }

    @GetMapping("/word-lessons/categories")
    public String wordLessonsCategories(Model model, Principal principal) {
        if(principal != null) {
            List<Category> wordLessonMainCategory = categoryService.mainWordLessonCategoryList(true);
            model.addAttribute("wordLessonMainCategory", wordLessonMainCategory);
        return "wordLessonsCategory";
        } return "redirect:/login";
    }

    @GetMapping("/word-lesson/{id}/lessons")
    public String wordLessons(@PathVariable("id")Long categoryId, Model model, Principal principal) {
        if(principal != null) {
            User user = userService.findByEmail(principal.getName());
            Category wordLessonCategory = categoryService.getCategoryToEditor(categoryId);
            List<WordLesson> wordLessons = wordLessonService.getWordLessonsCategory(user, categoryId);
            int sumWords = 0;
            for (WordLesson arr: wordLessonCategory.getWordLessons()) {
                sumWords += arr.getWords().size();
            }
            model.addAttribute("wordLessonCategory", wordLessonCategory);
            model.addAttribute("wordLessons", wordLessons);
            model.addAttribute("words", sumWords);
        return "wordLessons";
        } return "redirect:/login";
    }

    @GetMapping("/word-lesson/{id}")
    public String wordLesson(@PathVariable("id") Long wordLessonId,
                             Model model,
                             Principal principal) {
        if(principal != null) {
            Page<Word> wordsFromLesson = wordService.wordsFromLesson(0, 2, wordLessonId);
            if (wordsFromLesson.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", wordsFromLesson.getTotalPages());
            }
            model.addAttribute("words", wordsFromLesson.getContent());
            model.addAttribute("wordLessonId", wordLessonId);
        return "wordLesson";
        } return "redirect:/login";
    }



}
