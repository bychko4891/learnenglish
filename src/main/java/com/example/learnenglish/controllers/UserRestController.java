package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    private final WordUserService wordUserService;

    private final TranslationPairUserService translationPairUserService;
    private final UserWordLessonProgressService userWordLessonProgressService;

    private final HttpSession session;

    @PostMapping("/user/{id}/user-text-check")
    public ResponseEntity<CustomResponseMessage> mytext(@PathVariable("id") Long userId,
                                                        @RequestParam("userActive") boolean isChecked,
                                                        Principal principal) {
        if (principal != null) {
            session.setAttribute("userTextInLesson", isChecked);
            return ResponseEntity.ok(userService.setUserTextInLesson(userId, isChecked));
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (!userService.createUser(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Юзер з таким Email: " + user.getEmail() + " вже існує");
        }
        userService.createUser(user);
        return ResponseEntity.ok("Ви успішно створили аккаунт");
    }

    @PostMapping("/forgot-password/captcha")
    public ResponseEntity<String> handleCaptcha(@RequestParam("sum") int captchaSum) {
        session.setAttribute("captchaSum", captchaSum);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<CustomResponseMessage> forgotPassword(@RequestParam("email") String email,
                                                                @RequestParam("captcha") int captchaSum) {
        int captchaSumSession = (int) session.getAttribute("captchaSum");
        if (captchaSum == captchaSumSession) {
            return ResponseEntity.ok(userService.generatePassword(email));
        } else return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_CAPTCHA));
    }

    @PostMapping("/user/{userId}/edit")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> setUserInfo(@PathVariable("userId") Long userId,
                                              @RequestParam(value = "firstName", required = false) String firstName,
                                              @RequestParam(value = "lastName", required = false) String lastName,
                                              @RequestParam(value = "gender", required = false) String gender,
                                              Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            userService.updateUserInfo(userId, firstName, lastName, gender);
            session.setAttribute("userFirstName", firstName);
            session.setAttribute("userLastName", lastName);
            session.setAttribute("userGender", "[" + gender + "]");
            return ResponseEntity.ok("Інформація успішно оновлена");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/update-password")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> setUserPassword(@PathVariable("userId") Long userId,
                                                                 @RequestParam(value = "password") String oldPassword,
                                                                 @RequestParam(value = "newPassword") String newPassword,
                                                                 Principal principal) {
        if (principal != null) {
//            userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user-profile/delete")
    public ResponseEntity<CustomResponseMessage> userProfileRemove(@RequestParam("password") String userPassword,
                                                                   Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(userService.userProfileDelete(user, userPassword));
        }
        return ResponseEntity.notFound().build();
    }


    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @PostMapping("/user/word-plus")
    public ResponseEntity<CustomResponseMessage> wordUserPlus(@RequestParam("wordId") Long wordId,
                                                              Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(wordUserService.userWordPlus(user, wordId));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/user-word/remove")
    public ResponseEntity<CustomResponseMessage> userWordRemove(@RequestParam("wordId") Long wordId,
                                                                Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(wordUserService.userWordRemove(wordId, user));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/phrase-plus")
    public ResponseEntity<CustomResponseMessage> phraseUserPlus(@RequestParam("translationPairsId") Long translationPairsId,
                                                                Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());

            return ResponseEntity.ok(translationPairUserService.userPlusTranslationPairs(user, translationPairsId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/phrase/repetition-phrase-check")
    public ResponseEntity<CustomResponseMessage> isRepetitionPhrase(@RequestParam("isRepeatable") boolean isChecked,
                                                                    @RequestParam("translationPairsId") Long id,
                                                                    Principal principal) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(translationPairUserService.setRepetitionPhrase(id, userId, isChecked));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word/repetition-word-check")
    public ResponseEntity<CustomResponseMessage> isRepetitionWord(@RequestParam("isRepeatable") boolean isChecked,
                                                                  @RequestParam("wordId") Long id,
                                                                  Principal principal) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(wordUserService.setRepetitionWord(id, userId, isChecked));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user-phrase/remove")
    public ResponseEntity<CustomResponseMessage> userPhraseRemove(@RequestParam("phraseId") Long translationPairId,
                                                                  Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(translationPairUserService.userPhraseRemove(translationPairId, user));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word-lesson/{id}/start")
    public ResponseEntity<String> wordLessonStart(@PathVariable("id") Long wordLessonId, @RequestParam("start") boolean start,
                                                  Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            userWordLessonProgressService.startWordLesson(user, wordLessonId,start);
            return ResponseEntity.ok("tab2");
        }
        return ResponseEntity.notFound().build();
    }
}

