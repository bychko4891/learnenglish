package com.example.learnenglish.controllers;

import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import org.springframework.http.ResponseEntity;
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


//    @RequestMapping("/login")
//    public String login(@RequestParam(value = "error", required = false) String error,
//                        @RequestParam(value = "logout", required = false) String logout,
//                        Model model) {
//        if (error != null) {
//            model.addAttribute("error", "Не вірний логін, або пароль!");
//        } else if (logout != null) {
//            model.addAttribute("logout", "Ви вийшли із системи.");
//        }
//        return "login";
//    }

//    @GetMapping("/registration")
//    public String registration() {
//        return "registration";
//    }

    @PostMapping("/registration")
//    public String createUser(User user, Model model) {
    public void createUser(@RequestBody User user, Model model) {
        System.out.println(user);
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с Email: " + user.getEmail() + " уже существует");
//            return "registration";
        }
        userService.createUser(user);
//        return "redirect:/login";
    }

    @PostMapping("/user/{userId}/update")
    public ResponseEntity<User> userEditProfile(@PathVariable("userId") Long userId, @RequestParam(value = "firstName", required = false) String firstName,
                                                @RequestParam(value = "lastName", required = false) String lastName, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            System.out.println("id " + userId + " " + firstName + " " + lastName + "*****************************************************8");
            userService.updateUser(userId, firstName, lastName);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/update-password")
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

