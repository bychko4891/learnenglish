package com.example.learnenglish.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    @RequestMapping("/upload")
    public ResponseEntity<String> userUploadAvatar(@RequestParam(required = false, value = "file")
                                                   MultipartFile file, Principal principal) throws IOException {
        if (principal != null) {
            System.out.println(" *******************************************************");
            if (file != null) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                String uuidFile = UUID.randomUUID().toString();
                System.out.println(uuidFile + " *******************************************************");
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                System.out.println(resultFilename + " &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                file.transferTo(new File(uploadPath + "/" + resultFilename));
            }
        }
        return ResponseEntity.ok("filetest");
    }

}
