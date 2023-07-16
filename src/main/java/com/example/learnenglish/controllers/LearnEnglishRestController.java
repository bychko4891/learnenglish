package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LearnEnglishRestController {
    private final WordService wordService;

    public LearnEnglishRestController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/search-word")
    public ResponseEntity<List<DtoWordToUI>> searchWord(@RequestParam("searchTerm") String searchTerm) {
        System.out.println("yes");
        if (!searchTerm.isBlank()) {
            List<DtoWordToUI> wordsResult = wordService.searchWord(searchTerm);
            return ResponseEntity.ok(wordsResult);
        }

        return ResponseEntity.notFound().build();
    }
}
