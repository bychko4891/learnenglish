package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.model.PhraseLesson;
import com.example.learnenglish.service.CategoryService;
import com.example.learnenglish.service.PageApplicationService;
import com.example.learnenglish.service.PhraseLessonService;
import lombok.AllArgsConstructor;
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

    @GetMapping("/admin/phrase-lessons")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String lessonsListAdminPage(@RequestParam(value = "message", required = false) String message,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "8", required = false) int size,
                                       Principal principal,
                                       Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<PhraseLesson> lessonPage = phraseLessonService.getLessonsPage(page, size);
            if (lessonPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", lessonPage.getTotalPages());
            }
            model.addAttribute("message", message);
            model.addAttribute("lessons", lessonPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/phraseLessons";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/phrase-lessons/new-phrase-lesson")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String newLessonAdminPage(Principal principal) {
        if (principal != null) {
            try {
                long count = phraseLessonService.lastIdPhraseLesson() + 1;
                return "redirect:/admin/phrase-lessons/phrase-lesson-edit/" + count;
            } catch (NullPointerException e) {
                return "redirect:/admin/phrase-lessons/phrase-lesson-edit/1";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/phrase-lessons/phrase-lesson-edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String phraseLessonEdit(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainPhraseLessonCategories = categoryService.mainCategoryListByCategoryPage(true, CategoryPage.LESSON_PHRASES);
            try {
                PhraseLesson lesson = phraseLessonService.getPhraseLesson(id);
                model.addAttribute("phraseLesson", lesson);
            } catch (ObjectNotFoundException e) {
                model.addAttribute("phraseLesson", phraseLessonService.getNewPhraseLesson(id));
            }
            model.addAttribute("mainCategories", mainPhraseLessonCategories);
            return "admin/phraseLessonEdit";
        }
        return "redirect:/login";
    }
}
