package com.example.learnenglish.controllers;

import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (!userService.createUser(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Юзер з таким Email: " + user.getEmail() + " вже існує");
        }
        userService.createUser(user);
        return ResponseEntity.ok("Ви успішно створили аккаунт");
    }

    @PostMapping("/user/{userId}/edit")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> userDetailsEdit(@PathVariable("userId") Long userId, @RequestParam(value = "firstName", required = false) String firstName,
                                                @RequestParam(value = "lastName", required = false) String lastName, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            User user = userService.findByEmail(principal.getName());
            userService.updateUserInfo(userId, firstName, lastName);
            return ResponseEntity.ok("Інформація успішно оновлена");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/update-password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseStatus> userUpdatePassword(@PathVariable("userId") Long userId, @RequestParam(value = "password") String oldPassword,
                                   @RequestParam(value = "newPassword") String newPassword, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
//            System.out.println("id: " + userId + " old: " + oldPassword + " new: " + newPassword + " controller *****************************************************8");
            return  ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/delete")
    @PreAuthorize("hasAuthority('USER')")
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

