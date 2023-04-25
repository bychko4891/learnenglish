package com.example.learnenglish.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
public class ImageController {

    @Value("${upload.path}")
    private String uploadPath;
//    private final ImageRepository imageRepository;

//    @GetMapping("/images/{id}")
//    private ResponseEntity<?> getImageById(@PathVariable Long id) {
//        Image image = imageRepository.findById(id).orElse(null);
//        return ResponseEntity.ok()
//                .header("fileName", image.getOriginalFileName())
//                .contentType(MediaType.valueOf(image.getContentType()))
//                .contentLength(image.getSize())
//                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
//    }

//    @PostMapping("/user/{userId}/upload-avatar")
//@GetMapping("/upload")
//    public String userUploadAvatar(@RequestParam("file") MultipartFile file, Principal principal, Model model) throws IOException {
////        if (file != null) {
////            File uploadDir = new File(uploadPath);
////            if (!uploadDir.exists()) uploadDir.mkdir();
////            String uuidFile = UUID.randomUUID().toString();
////            System.out.println(uuidFile + " *******************************************************");
////            String resultFilename = uuidFile + "." + file.getOriginalFilename();
////            file.transferTo(new File(uploadPath + "/" + resultFilename));
////        }
//        return "filetest";
//    }
    @PostMapping("/uploadadd")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

//        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "filetest";
    }
//    public ResponseEntity<File> upload(@RequestParam MultipartFile attachment) {
////        try {
////            return new ResponseEntity<>(fileService.upload(attachment), HttpStatus.CREATED);
////        } catch (IOException e) {
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
////        }
//    }
}
