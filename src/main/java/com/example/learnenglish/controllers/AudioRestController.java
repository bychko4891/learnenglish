package com.example.learnenglish.controllers;

import com.example.learnenglish.service.AudioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AudioRestController {
    private final AudioService audioService;

    public AudioRestController(AudioService audioService) {
        this.audioService = audioService;
    }


    @GetMapping("/audio/{fileName:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = audioService.loadFileAsResource(fileName);
        return ResponseEntity.ok(resource);
    }

}
