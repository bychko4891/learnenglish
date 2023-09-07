package com.example.learnenglish.controllers;
/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

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

    @RequestMapping("endSlide")
    public String getEndSlid() {
        return "fragmentsPages :: endSlide";
    }
    @RequestMapping("endSlideSpelling")
    public String getEndSlideSpelling() {
        return "fragmentsPages :: endSlideSpelling";
    }

    @RequestMapping("successRegistrationFragment")
    public String getContent5() {
        return "fragmentsPages :: successRegistrationFragment";
    }

    @RequestMapping("wordLessonReview")
    public String wordLessonReview() {
        return "fragmentsPages :: wordLessonReview";
    }

    @RequestMapping("wordLessonSpelling")
    public String wordLessonSpelling() {
        return "fragmentsPages :: wordLessonSpelling";
    }


    @RequestMapping("wordLessonAudit")
    public String wordLessonAudit() {
        return "fragmentsPages :: wordLessonAudit";
    }
    @RequestMapping("slideReview")
    public String slideReview() {
        return "fragmentsPages :: slideReview";
    }

    @RequestMapping("slideSpelling")
    public String slide() {
        return "fragmentsPages :: slideSpelling";
    }

    @RequestMapping("slideAudit")
    public String slideAudit() {
        return "fragmentsPages :: slideAudit";
    }

    @RequestMapping("slideAuditRadios")
    public String slideAuditRadios() {
        return "fragmentsPages :: slideAuditRadios";
    }
    @RequestMapping("wordLessonAuditResult")
    public String wordLessonAuditResult() {
        return "fragmentsPages :: wordLessonAuditResult";
    }
}