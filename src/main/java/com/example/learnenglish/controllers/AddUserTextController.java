package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.model.users.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "https://localhost:8443")
public class AddUserTextController {
    private final DtoTranslationPair createTranslationPair;

    public AddUserTextController(DtoTranslationPair createTranslationPair) {
        this.createTranslationPair = createTranslationPair;
    }
//    private final ValidationAndSaveTranslationPair checkingTextAndSaveService;
//    public AddUserTextController(ValidationAndSaveTranslationPair checkingTextAndSaveService) {
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
    @PostMapping (path = "/englishADD")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> textADD(@RequestBody DtoTranslationPair dtoTranslationPair, Model model, User user) {
        System.out.println(dtoTranslationPair + " jcnjcnjkcncvvv ********************************");
        model.addAttribute("user", user);
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

