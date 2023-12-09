package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.PhraseAndUserService;
import com.example.learnenglish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PhraseAndUserRestController {

    private final PhraseAndUserService phraseAndUserService;

    private final UserService userService;

    @PostMapping("/user/phrase-plus") // змінити адресу !!!!
    public ResponseEntity<CustomResponseMessage> phraseUserPlus(@RequestParam("translationPairsId") Long translationPairsId,
                                                                Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());

            return ResponseEntity.ok(phraseAndUserService.userPlusTranslationPairs(user, translationPairsId));
        }
        return ResponseEntity.notFound().build();
    }

}
