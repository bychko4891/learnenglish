package com.example.learnenglish.controllers;

import com.example.learnenglish.service.PhraseUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PhraseUserController {

    private PhraseUserService service;



}
