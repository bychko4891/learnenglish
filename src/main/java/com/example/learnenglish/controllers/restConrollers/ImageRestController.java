package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.FileFormatException;
import com.example.learnenglish.service.ImagesService;
import com.example.learnenglish.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ImageRestController {
    private static final Logger logger = LoggerFactory.getLogger(ImageRestController.class);

    private final ImagesService imagesService;


    @GetMapping("/web-image/{fileName:.+}")
    public ResponseEntity<byte[]> webImage(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = imagesService.loadWebImages(fileName);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG); // встановити тип контенту як image/jpeg, або image/png, залежно від формату зображення
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/word-image/{fileName:.+}")
    public ResponseEntity<byte[]> wordImage(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = imagesService.loadWordImages(fileName);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG); // встановити тип контенту як image/jpeg, або image/png, залежно від формату зображення
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
    @GetMapping("/category-image/{fileName:.+}")
    public ResponseEntity<byte[]> categoryImage(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = imagesService.loadCategoryImages(fileName);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG); // встановити тип контенту як image/jpeg, або image/png, залежно від формату зображення
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
