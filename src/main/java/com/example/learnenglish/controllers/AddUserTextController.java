package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.CreateTranslationPair;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.users.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddUserTextController {
    private final CreateTranslationPair createTranslationPair;

    public AddUserTextController(CreateTranslationPair createTranslationPair) {
        this.createTranslationPair = createTranslationPair;
    }
//    private final CheckingTextAndSaveService checkingTextAndSaveService;
//    public AddUserTextController(CheckingTextAndSaveService checkingTextAndSaveService) {
//        this.checkingTextAndSaveService = checkingTextAndSaveService;
//    }

//    @GetMapping(path = "/englishADD")
//    public void textADD(ttpServletRequest request, HttpServletResponse response) {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        PrintWriter printWriter = null;
//        try {
//            printWriter = response.getWriter();
//            printWriter.println(new ObjectMapper().writeValueAsString(checkingTextAndSaveService.checkingRequestText(request.getParameter("ukrText"), request.getParameter("engText"))));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            printWriter.close();
//        }
////            System.out.println("It is OK!!! " + ukrText + " " + engText);
//    }
    @RequestMapping(path = "/lesson/{lessonId}/user/{id}/englishADD")
    public ResponseEntity textADD(@PathVariable("lessonId") Long lessonId, @PathVariable("id") Long id,
                                  @RequestBody CreateTranslationPair createTranslationPair, Lesson lesson, User user, HttpServletRequest request) {
        lessonId = lesson.getId();
        id = user.getId();
        String test = request.getRequestURI();
        System.out.println(createTranslationPair + " | " + test);
//            System.out.println("It is OK!!! " + ukrText + " " + engText);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

