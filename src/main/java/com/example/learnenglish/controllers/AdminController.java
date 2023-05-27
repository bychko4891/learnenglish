package com.example.learnenglish.controllers;

import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.TextOfAppPageService;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final LessonService lessonService;
    private final UserService userService;
    private final TextOfAppPageService textOfAppPageService;

    public AdminController(LessonService lessonService, UserService userService, TextOfAppPageService textOfAppPageService) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.textOfAppPageService = textOfAppPageService;
    }


    @GetMapping
    public String adminPage(Model model, Principal principal) {
        if (principal != null) {

            User user = userService.findByEmail(principal.getName());
//            List<Lesson> lessons = lessonService.lessonsListToAdminPage();
            model.addAttribute("user", user);
//            model.addAttribute("lessons", lessons);
            return "admin-page";
        }
        return "redirect:/login";
    }

    @GetMapping("/texts-of-app-pages")
    public String getTextsOfAppPages(Model model, Principal principal) {
        if (principal != null) {
            List<TextOfAppPage> textOfAppPageList = textOfAppPageService.getAppTextPageList();
            model.addAttribute("textOfAppPageList", textOfAppPageList);
            return "textsOfAppPages";
        }
        return "redirect:/login";
    }
    @GetMapping("/texts-of-app-pages/new-app-text-page")
    public String appInfoListAdminPage(Model model, Principal principal) {
        if (principal != null) {
            Long count = textOfAppPageService.countTextOfAppPage() + 1;
            return "redirect:/admin-page/text-of-app-page/" + count + "/new-app-text-page";
        }
        return "redirect:/login";
    }

    @GetMapping("/text-of-app-page/{id}/new-app-text-page")
    public String lessonsEdit1(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
//            lesson = lessonService.findById(id);
            TextOfAppPage textOfAppPage = new TextOfAppPage();
            textOfAppPage.setId(id);
            textOfAppPage.setName("Enter name");
            textOfAppPage.setText("Enter text");
            model.addAttribute("textOfAppPage", textOfAppPage);
            return "newAppTextPage";
        }
        return "redirect:/login";
    }

//    @GetMapping("/texts-of-app-pages/new-app-text-page")
//    public String lessonsEdit(Model model, Principal principal) {
//        if (principal != null) {
////            lesson = lessonService.findById(id);
//            TextOfAppPage textOfAppPage = new TextOfAppPage();
//            textOfAppPage.setName("Enter name");
//            textOfAppPage.setText("Enter text");
//            model.addAttribute("appTextPage", textOfAppPage);
//            return "newAppTextPage";
//        }
//        return "redirect:/login";
//    }

    @GetMapping("/users")
    public String usersListAdminPage(Model model, Principal principal,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (principal != null) {
            Page<User> userPage = userService.getUsersPage(page, size);
            model.addAttribute("users", userPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userPage.getTotalPages());
            return "users";
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons")
    public String lessonsListAdminPage(Model model, Principal principal,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (principal != null) {
            Page<Lesson> lessonPage = lessonService.getLessonsPage(page, size);
            model.addAttribute("lessons", lessonPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", lessonPage.getTotalPages());
            return "lessons";
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons/lesson/{id}")
    public String lessonEditPage(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {
        if (principal != null) {
            id = lesson.getId();
            lesson = lessonService.findById(id);
            if (lesson.getLessonInfo() == null || lesson.getName().equals("newLesson")) {
                lesson.setName("Заняття № " + lesson.getId());
                lesson.setLessonInfo("Опис заняття");
                model.addAttribute("lesson", lesson);
                return "lesson-edit";
            } else {
                model.addAttribute("lesson", lesson);
                return "lesson-edit";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons/lesson/{id}/edit")
    public String lessonsEdit(@PathVariable("id") Long id, Model model, Lesson lesson, Principal principal) {
        if (principal != null) {
            lesson = lessonService.findById(id);
            model.addAttribute("lesson", lesson);
            return "lesson-edit";
        }
        return "redirect:/login";
    }
}