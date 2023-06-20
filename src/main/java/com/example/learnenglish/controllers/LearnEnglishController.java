package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.service.LessonService;
import com.example.learnenglish.service.PageApplicationService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
//@RequestMapping(method = RequestMethod.GET)
public class LearnEnglishController {
    private final HttpSession session;
    private final UserService userService;
    private final LessonService lessonService;
    private final PageApplicationService pageApplicationService;

    public LearnEnglishController(HttpSession session,
                                  UserService userService,
                                  LessonService lessonService,
                                  PageApplicationService pageApplicationService) {
        this.session = session;
        this.userService = userService;
        this.lessonService = lessonService;
        this.pageApplicationService = pageApplicationService;
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
            model.addAttribute("userId", userId);
            return "redirect:/user/" + userId;
        } else {
            PageApplication pageApplication = pageApplicationService.getPageApplication(5l);
            if(pageApplication.getTextOfAppPage() != null){
                model.addAttribute("pageText", pageApplication.getTextOfAppPage().getText());
            } else {
                model.addAttribute("pageText", "No text in this page");
            }
            model.addAttribute("title", "About the app Learn English");
            return "index";
        }
    }



    @GetMapping("/about-the-app")
    public String aboutApp(Model model) {
        model.addAttribute("title", "About the app Learn English");
        PageApplication pageApplication = pageApplicationService.getPageApplication(3l);
        if(pageApplication.getTextOfAppPage() != null){
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
            if(pageApplication.getTextOfAppPage() != null){
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

}
