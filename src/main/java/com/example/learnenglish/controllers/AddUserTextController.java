package com.example.learnenglish.controllers;

import com.example.learnenglish.service.CheckingTextAndSaveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class AddUserTextController {
    private final CheckingTextAndSaveService checkingTextAndSaveService;
    public AddUserTextController(CheckingTextAndSaveService checkingTextAndSaveService) {
        this.checkingTextAndSaveService = checkingTextAndSaveService;
    }

    @GetMapping(path = "/englishADD")
    public void textADD(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.println(new ObjectMapper().writeValueAsString(checkingTextAndSaveService.checkingRequestText(request.getParameter("ukrText"), request.getParameter("engText"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            printWriter.close();
        }
//            System.out.println("It is OK!!! " + ukrText + " " + engText);
    }
}

