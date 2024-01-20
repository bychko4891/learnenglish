package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.FileStorageException;
import com.example.learnenglish.exception.MyFileNotFoundException;
import com.example.learnenglish.property.FileStorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Map<String, Path> directoryMappings = new HashMap<>();

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        directoryMappings.put(fileStorageProperties.getUploadDir(), createDirectory(fileStorageProperties.getUploadDir()));
        directoryMappings.put(fileStorageProperties.getUploadAudio(), createDirectory(fileStorageProperties.getUploadAudio()));
        directoryMappings.put(fileStorageProperties.getUploadUserAvatar(), createDirectory(fileStorageProperties.getUploadUserAvatar()));
        directoryMappings.put(fileStorageProperties.getUploadWordImage(), createDirectory(fileStorageProperties.getUploadWordImage()));
        directoryMappings.put(fileStorageProperties.getUploadCategoryImage(), createDirectory(fileStorageProperties.getUploadCategoryImage()));
    }

    private Path createDirectory(String path) {
        Path storagePath = Paths.get(path).toAbsolutePath().normalize();

        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
            } catch (IOException e) {
                throw new FileStorageException("Could not create directory: " + storagePath, e);
            }
        }
        return storagePath;
    }

    public String storeFile(MultipartFile file, String storagePath, String firstName) throws IOException {
        if (directoryMappings.containsKey(storagePath)) {
            Path directoryPath = directoryMappings.get(storagePath);
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            String contentType = Objects.requireNonNull(file.getContentType()).replaceAll("^[a-zA-Z]+/", ".");
            if(contentType.equalsIgnoreCase(".mpeg")) contentType = ".mp3";
            String uuidFile = UUID.randomUUID().toString();
            String newFileName = firstName + "_" + uuidFile + contentType;
            Path targetLocationAudio = directoryPath.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocationAudio, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } else {
            throw new IllegalArgumentException("Invalid directory name: " + storagePath);
        }
    }

    public Resource loadFileAsResource(String fileName, String storagePath) {
        try {
            Path directoryPath = directoryMappings.get(storagePath);
            Path filePath = directoryPath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("Error load file: " + fileName, ex);
        }
    }

    public void deleteFileFromStorage(String fileName, String storagePath) {
        Path directoryPath = directoryMappings.get(storagePath);
        Path targetLocation = directoryPath.resolve(fileName);
        try {
            Files.delete(targetLocation);
        } catch (IOException e) {
//            throw new RuntimeException("Image not found");
            System.out.println("Error in 'deleteFileFromStorage' methods! File not found: " + fileName);
        }
    }
}
