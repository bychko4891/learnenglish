package com.example.learnenglish.controllers;

import com.example.learnenglish.exception.FileFormatException;
import com.example.learnenglish.model.users.UserAvatar;
import com.example.learnenglish.service.UserAvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
@RestController
public class UserAvatarController {
    private static final Logger logger = LoggerFactory.getLogger(UserAvatarController.class);


    private final UserAvatarService userAvatarService;

    public UserAvatarController(UserAvatarService userAvatarService) {
        this.userAvatarService = userAvatarService;
    }
    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        if (principal != null) {
            String contentType = file.getContentType();
            if (!contentType.equals("image/png")) {
                // Відкидаємо всі файли, які не є PNG
                throw new FileFormatException("Дозволено тільки файли з розширенням .png");
            }
            String fileName = userAvatarService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();
            System.out.println(fileDownloadUri + " URI ******************************");

//        return new UserAvatar(avatarUri);
        }
    }
//    @PostMapping("/uploadFile")
//    public UserAvatar uploadFile(@RequestParam("file") MultipartFile file) {
//        String fileName = userAvatarService.storeFile(file);
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        return new UserAvatar(avatarUri);
//    }

    @ExceptionHandler(FileFormatException.class)
    public String handleFileFormatException(FileFormatException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }
}
