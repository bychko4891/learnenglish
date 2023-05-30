package com.example.learnenglish.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("lessonFragments")
public class ContentController {

    @RequestMapping("fragment1")
    public String getContent1() {
        return "lessonFragments :: fragment1";
    }

    @RequestMapping("fragment2")
    public String getContent2() {
        return "lessonFragments :: fragment2";
    }

    @RequestMapping("fragment3")
    public String getContent3() {
        return "lessonFragments :: fragment3";
    }

    @RequestMapping("fragment4")
    public String getContent4() {
        return "lessonFragments :: fragment3";
    }
}
