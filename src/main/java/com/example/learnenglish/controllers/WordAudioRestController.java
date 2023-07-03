package com.example.learnenglish.controllers;

import com.example.learnenglish.service.WordAudioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WordAudioRestController {
    private final WordAudioService wordAudioService;

    public WordAudioRestController(WordAudioService wordAudioService) {
        this.wordAudioService = wordAudioService;
    }


    @GetMapping("/audio/{fileName:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = wordAudioService.loadFileAsResource(fileName);
        return ResponseEntity.ok(resource);
    }

}
