package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordCategory;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
//@RequestMapping(method = RequestMethod.GET)
public class LearnEnglishController {
    private final HttpSession session;
    private final UserService userService;
    private final LessonService lessonService;
    private final PageApplicationService pageApplicationService;
    private final WordCategoryService wordCategoryService;
    private final WordService wordService;

    public LearnEnglishController(HttpSession session,
                                  UserService userService,
                                  LessonService lessonService,
                                  PageApplicationService pageApplicationService,
                                  WordCategoryService wordCategoryService,
                                  WordService wordService) {
        this.session = session;
        this.userService = userService;
        this.lessonService = lessonService;
        this.pageApplicationService = pageApplicationService;
        this.wordCategoryService = wordCategoryService;
        this.wordService = wordService;
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
            model.addAttribute("userId", userId);
            return "redirect:/user/" + userId;
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
            } else {
                model.addAttribute("mainBottom", "No text in this page");
            }
            model.addAttribute("title", "Англійська за 16 годин - English Learn Application");
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
            lesson = lessonService.findById(lessonId);
            session.setAttribute("lessonId", lesson.getId());
            model.addAttribute("lessonId", lesson.getId());
            model.addAttribute("lesson", lesson);
            return "lesson";
        }
        return "redirect:/login";
    }

    @GetMapping("/words-main-category")
    public String wordsMainCategories(Model model) {
        List<WordCategory> wordsMainCategories = wordCategoryService.mainWordCategoryList(true);
        if (wordsMainCategories != null) {
            model.addAttribute("wordsMainCategories", wordsMainCategories);
        }
        return "wordsMainCategory";
    }

    @GetMapping("/words-main-category/{id}")
    public String wordsSubcategoriesFromMainCategories(@PathVariable Long id, Model model) {
        WordCategory mainWordsCategory = wordCategoryService.getWordCategoryToEditor(id);

        if (mainWordsCategory.isViewSubcategoryFullNoInfoOrNameAndInfo()) {
            return "wordsSubcategoryNameAndInfo";
        } else {
            List<WordCategory> wordsSubCategoriesAndSubSubInMainCategory = wordCategoryService.getSubcategoriesAndSubSubcategoriesInMainCategory(id);
            model.addAttribute("wordsSubCategories", wordsSubCategoriesAndSubSubInMainCategory);
            model.addAttribute("mainCategoryId", mainWordsCategory.getId());
            return "wordsSubcategoryFullAndNoInfo";
        }
    }

    @GetMapping("/subcategory/{id}")
    public String wordsSubcategories(@PathVariable Long id, Model model) {
        WordCategory subcategory = wordCategoryService.getWordCategoryToEditor(id);
        WordCategory parentCategory = subcategory.getParentCategory();
        model.addAttribute("words", subcategory.getWords());
        model.addAttribute("subId", subcategory.getId());
        model.addAttribute("mainId", parentCategory.getParentCategory().getId());
//        if (subcategory.isViewSubcategoryFullNoInfoOrNameAndInfo()) {
//            return "wordsSubcategoryNameAndInfo";
//        } else {
////            List<WordCategory> wordsSubCategoriesAndSubSubInMainCategory = wordCategoryService.getSubcategoriesAndSubSubcategoriesInMainCategory(id);
////            model.addAttribute("wordsSubCategories", wordsSubCategoriesAndSubSubInMainCategory);
////            model.addAttribute("mainCategoryId", mainWordsCategory.getId());
//            return "wordsSubcategoryView";
//        }
        return "wordsSubcategoryView";
    }
    @GetMapping("/word/{id}")
    public String word(@PathVariable Long id, Model model) {
        Word word = wordService.getWord(id);
        WordCategory wordCategory = word.getWordCategory().getParentCategory();
        model.addAttribute("word", word);
        model.addAttribute("mainId", wordCategory.getParentCategory().getId());

        return "word";
    }

}
