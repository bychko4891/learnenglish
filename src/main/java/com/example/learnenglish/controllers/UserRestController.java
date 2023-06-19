package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.UserService;
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

    private final HttpSession session;

    @PostMapping("/user/{id}/user-text-check")
    public ResponseEntity<ResponseMessage> mytext(@PathVariable("id") Long userId,
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
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestParam("email") String email,
                                                          @RequestParam("captcha") int captchaSum) {
        int captchaSumSession = (int) session.getAttribute("captchaSum");
        if (captchaSum == captchaSumSession) {
            return ResponseEntity.ok(userService.generatePassword(email));
        } else return ResponseEntity.ok(new ResponseMessage(Message.ERROR_CAPTCHA));
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
    public ResponseEntity<ResponseMessage> setUserPassword(@PathVariable("userId") Long userId,
                                                           @RequestParam(value = "password") String oldPassword,
                                                           @RequestParam(value = "newPassword") String newPassword,
                                                           Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/delete")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<User> userDelete(@PathVariable("userId") Long userId,
                                           Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/";
    }


}

