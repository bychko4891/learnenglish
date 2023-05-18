package com.example.learnenglish.controllers;

import com.example.learnenglish.exception.FileFormatException;
import com.example.learnenglish.service.UserAvatarService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
@RestController
public class UserAvatarController {
    private static final Logger logger = LoggerFactory.getLogger(UserAvatarController.class);
    private final UserAvatarService userAvatarService;
    private final UserService userService;
    private final HttpSession session;
    public UserAvatarController(UserAvatarService userAvatarService, HttpSession session, UserService userService) {
        this.userAvatarService = userAvatarService;
        this.session = session;
        this.userService = userService;
    }
    @PostMapping("/user/{userId}/upload-avatar")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> uploadFile(@PathVariable("userId") Long userId, @RequestParam("file") MultipartFile file, Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            String contentType = file.getContentType();
            if (contentType.equals("image/png")) {
                // Відкидаємо всі файли, які не є PNG
                String fileName = userAvatarService.storeFile(file, userId);
                session.setAttribute("avatarName", fileName);
                return ResponseEntity.ok("Ok");
            } else throw new FileFormatException("Дозволено тільки файли з розширенням .png");
//            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/user/"+ userId+ "/avatar/")
//                    .path(fileName)
//                    .toUriString();

//        return new UserAvatar(avatarUri);
        }
        return ResponseEntity.ok("Дозволено тільки файли з розширенням .png");
    }


    @GetMapping("/avatar/{fileName:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = userAvatarService.loadFileAsResource(fileName);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // встановити тип контенту як image/jpeg, або image/png, залежно від формату зображення
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


    @ExceptionHandler(FileFormatException.class)
    public String handleFileFormatException(FileFormatException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }
}
