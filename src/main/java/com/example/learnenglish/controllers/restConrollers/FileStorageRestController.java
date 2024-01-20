package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.service.FileStorageService;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@Data
public class FileStorageRestController {

    @Value("${file.upload-audio}")
    private String audioStorePath;

    @Value("${file.upload-user-avatar}")
    private String userAvatarStorePath;

    private final FileStorageService fileStorageService;

    @GetMapping("/audio/{audioFileName:.+}")
    public ResponseEntity<Resource> getAudioFileFromStorage(@PathVariable String audioFileName) {
        Resource resource = fileStorageService.loadFileAsResource(audioFileName, audioStorePath);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/avatar/{imageFileName:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String imageFileName) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(imageFileName, userAvatarStorePath);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
