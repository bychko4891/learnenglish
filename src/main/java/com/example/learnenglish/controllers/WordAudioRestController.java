package com.example.learnenglish.controllers;

import com.example.learnenglish.service.WordAudioService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class WordAudioRestController {
    private final WordAudioService wordAudioService;

    public WordAudioRestController(WordAudioService wordAudioService) {
        this.wordAudioService = wordAudioService;
    }


    @GetMapping("/audio/{fileName:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = wordAudioService.loadFileAsResource(fileName);


//        InputStream in = resource.getInputStream();
//        System.out.println("123");
//        byte[] imageBytes = IOUtils.toByteArray(in);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG); // встановити тип контенту як image/jpeg, або image/png, залежно від формату зображення
//        headers.setContentLength(imageBytes.length);
//        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());

        return ResponseEntity.ok(resource);
    }

}
