package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsestatus.ResponseStatus;
import com.example.learnenglish.service.TranslationPairValidationAndSaveService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

//@RestController
//@CrossOrigin(origins = "https://localhost:8443")
public class AddUserTextController {

    private final TranslationPairValidationAndSaveService validationTranslationPair;

    public AddUserTextController(TranslationPairValidationAndSaveService validationTranslationPair) {
        this.validationTranslationPair = validationTranslationPair;
    }
//    @GetMapping(path = "/englishJSON")
    public void response2(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
//            printWriter.println(new ObjectMapper().writeValueAsString(randomTranslationPairService.translationPairRandom()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            printWriter.close();
        }
    }
//    private final TranslationPairValidationAndSaveService checkingTextAndSaveService;
//    public AddUserTextController(TranslationPairValidationAndSaveService checkingTextAndSaveService) {
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
//    @PostMapping (path = "/englishADD")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseStatus> textADD(@Validated @RequestBody DtoTranslationPair dtoTranslationPair, Model model, User user) {
        validationTranslationPair.validationTranslationPair(dtoTranslationPair);
//        System.out.println(dtoTranslationPair.getEngText());
//        System.out.println(dtoTranslationPair.getUkrText());
//        System.out.println(dtoTranslationPair.toString());
//        model.addAttribute("user", user);
//        String test = request.getRequestURI();
//        System.out.println(dtoTranslationPair + " | " + test);
//            System.out.println("It is OK!!! " + ukrText + " " + engText);
//        if (principal != null) {
//            // Отримати id залогіненого користувача
//            userId = userService.findByEmail(principal.getName()).getId();
//            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("userId", userId);
//            model.addAttribute("user", user);
////            return "user-info";
//        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

