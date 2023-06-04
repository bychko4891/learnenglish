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
    public String adminPage(Principal principal) {
        if (principal != null) {
//            User user = userService.findByEmail(principal.getName());

//            model.addAttribute("user", user);

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
    public String appInfoListAdminPage(Principal principal) {
        if (principal != null) {
            Long count = textOfAppPageService.countTextOfAppPage() + 1;
            return "redirect:/admin-page/text-of-app-page/" + count + "/new-text-of-app-page-in-editor";
        }
        return "redirect:/login";
    }

    @GetMapping("/text-of-app-page/{id}/new-text-of-app-page-in-editor")
    public String newTextOfAppPage(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
//            lesson = lessonService.findById(id);
            TextOfAppPage textOfAppPage = new TextOfAppPage();
            textOfAppPage.setId(id);
            textOfAppPage.setName("Enter name");
            textOfAppPage.setText("Enter text");
            model.addAttribute("textOfAppPage", textOfAppPage);
            return "textOfAppPageInEditor";
        }
        return "redirect:/login";
    }

    @GetMapping("/text-of-app-page/{id}/text-of-app-page-in-editor")
    public String textOfAppPageEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            TextOfAppPage textOfAppPage = textOfAppPageService.findByIdTextOfAppPage(id);
            model.addAttribute("textOfAppPage", textOfAppPage);
            return "textOfAppPageInEditor";
        }
        return "redirect:/login";
    }

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

    @GetMapping("/lessons/new-lesson")
    public String newLessonAdminPage(Principal principal) {
        if (principal != null) {
            Long count = lessonService.countLessons() + 1;
            return "redirect:/admin-page/lesson/" + count + "/new-lesson-in-editor";
        }
        return "redirect:/login";
    }

    @GetMapping("/lesson/{id}/new-lesson-in-editor")
    public String newLesson(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
//            lesson = lessonService.findById(id);
            Lesson lesson = new Lesson();
            lesson.setId(id);
            lesson.setName("Заняття № " + id);
            lesson.setLessonInfo("Опис заняття");
            model.addAttribute("lesson", lesson);
            return "lessonInEditor";
        }
        return "redirect:/login";
    }

    @GetMapping("/lesson/{id}/lesson-in-editor")
    public String lessonEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            Lesson lesson = lessonService.findById(id);
            model.addAttribute("lesson", lesson);
            return "lessonInEditor";
        }
        return "redirect:/login";
    }

}