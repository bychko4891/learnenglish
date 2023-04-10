package com.example.learnenglish.controllers;


import org.springframework.ui.Model;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }
    @PostMapping("/registration")
    public String createUser(User user, Model model){
        if(!userService.createUser(user)){
            model.addAttribute("errorMessage","Пользователь с Email: " + user.getEmail() + " уже существует");
            return "registration";
        }
//        userService.createUser(user);
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String security(){
        return "redirect:/";
    }
//    @RequestMapping("/login")
//    public String login(HttpServletRequest request, HttpServletResponse response) {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        String userName = request.getParameter("username");
//        String userPassword = request.getParameter("password");
//        System.out.println(userName + " : " + userPassword);
//        translationPairService.findUserByEmail(userName);
//        return "login";
//    }
}

