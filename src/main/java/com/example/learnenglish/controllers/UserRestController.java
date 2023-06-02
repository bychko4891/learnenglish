package com.example.learnenglish.controllers;

import com.example.learnenglish.responsestatus.ResponseStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
public class UserRestController {
    private int captchaSum;

    private final UserService userService;

    private final HttpSession session;

    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (!userService.createUser(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Юзер з таким Email: " + user.getEmail() + " вже існує");
        }
        userService.createUser(user);
        return ResponseEntity.ok("Ви успішно створили аккаунт");
    }

    @PostMapping("/forgot-password/captcha")
    public ResponseEntity<String> handleCaptcha(@RequestParam("sum") int sum) {
        captchaSum = sum;
        System.out.println(captchaSum + " ************************************");
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/user/{userId}/edit")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> setUserInfo(@PathVariable("userId") Long userId, @RequestParam(value = "firstName", required = false) String firstName,
                                              @RequestParam(value = "lastName", required = false) String lastName, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            userService.updateUserInfo(userId, firstName, lastName);
            session.setAttribute("userFirstName", firstName);
            session.setAttribute("userLastName", lastName);
            return ResponseEntity.ok("Інформація успішно оновлена");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/update-password")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseStatus> setUserPassword(@PathVariable("userId") Long userId, @RequestParam(value = "password") String oldPassword,
                                                          @RequestParam(value = "newPassword") String newPassword, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
//            System.out.println("id: " + userId + " old: " + oldPassword + " new: " + newPassword + " controller *****************************************************8");
            return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/delete")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<User> userDelete(@PathVariable("userId") Long userId, Principal principal) {
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

