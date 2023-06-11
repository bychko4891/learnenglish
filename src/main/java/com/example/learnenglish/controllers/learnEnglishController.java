package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
//@RequestMapping(method = RequestMethod.GET)
public class learnEnglishController {
    private final HttpSession session;
    private final UserService userService;
    private final LessonService lessonService;

    public learnEnglishController(HttpSession session,
                                  UserService userService,
                                  LessonService lessonService) {
        this.session = session;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            // Отримати id залогіненого користувача
            Long userId = userService.findByEmail(principal.getName()).getId();
            model.addAttribute("userId", userId);
            // Перенаправити на сторінку з профілем користувача з його id
            return "redirect:/user/" + userId;
        } else {
            model.addAttribute("title", "About the app Learn English");
            model.addAttribute("main_title", "Main page");
            // Перенаправити на сторінку з увійти / зареєструватися
            return "index";
        }
    }



    @GetMapping("/about-the-app")
    public String aboutApp(Model model) {
        model.addAttribute("title", "About the app Learn English");
        return "about-the-app";
    }


    @GetMapping("/lesson/{lessonId}")
    public String lessonPage(@PathVariable("lessonId") Long lessonId,
                             Lesson lesson, Principal principal, Model model) {
        model.addAttribute("title", "About the app Learn English");
        if (principal != null) {
            lesson = lessonService.findById(lessonId);
            session.setAttribute("lessonId", lesson.getId());
            model.addAttribute("lessonId", lesson.getId());
            model.addAttribute("lesson", lesson);
            return "lesson";
        }
        return "redirect:/login";
    }

}
