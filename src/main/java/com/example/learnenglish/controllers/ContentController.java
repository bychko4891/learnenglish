package com.example.learnenglish.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("content")
public class ContentController {

    @RequestMapping("content1")
    public String getContent1() {
        return "content :: content1";
    }

    @RequestMapping("content2")
    public String getContent2() {
        return "content :: content2";
    }

    @RequestMapping("content3")
    public String getContent3() {
        return "content :: content3";
    }

    @RequestMapping("content4")
    public String getContent4() {
        return "content :: content3";
    }
}
