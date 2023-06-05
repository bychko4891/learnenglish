package com.example.learnenglish.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("fragmentsPages")
public class ContentController {

    @RequestMapping("lessonFragment1")
    public String getContent1() {
        return "fragmentsPages :: lessonFragment1";
    }

    @RequestMapping("lessonFragment2")
    public String getContent2() {
        return "fragmentsPages :: lessonFragment2";
    }

    @RequestMapping("lessonFragment3")
    public String getContent3() {
        return "fragmentsPages :: lessonFragment3";
    }

    @RequestMapping("lessonFragment4")
    public String getContent4() {
        return "fragmentsPages :: lessonFragment4";
    }
    @RequestMapping("successRegistrationFragment")
    public String getContent5() {
        return "fragmentsPages :: successRegistrationFragment";
    }
}
