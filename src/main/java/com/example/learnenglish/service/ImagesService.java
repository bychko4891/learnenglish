package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.FileStorageException;
import com.example.learnenglish.exception.MyFileNotFoundException;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.model.users.Images;
import com.example.learnenglish.property.FileStorageProperties;
import com.example.learnenglish.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImagesService {
    private final Path fileStorageLocation;
    private final Path storageLocationUserAvatar;
    private final ImagesRepository imagesRepository;

    @Autowired
    public ImagesService(FileStorageProperties fileStorageProperties, ImagesRepository imagesRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.storageLocationUserAvatar = Paths.get(fileStorageProperties.getUploadUserAvatar())
                .toAbsolutePath().normalize();
        this.imagesRepository = imagesRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.storageLocationUserAvatar);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//            System.out.println(ex.getMessage());
        }
    }


    public String storeFile(MultipartFile file, Long userId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + ".png";
            Path targetLocation = this.storageLocationUserAvatar.resolve(resultFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            saveUserAvatar(userId, resultFilename);
            return resultFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//            System.out.println(ex.getMessage());
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.storageLocationUserAvatar.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);

            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
//            System.out.println(ex.getMessage());
        }
    }

    private void saveUserAvatar(Long userId, String userAvatarName) {
        Optional<Images> optionalUserAvatar = imagesRepository.findById(userId);
        if (optionalUserAvatar.isPresent()) {
            Images avatar = optionalUserAvatar.get();
            if (avatar.getImageName() != null) deleteImageToDirektori(avatar.getImageName());
            avatar.setImageName(userAvatarName);
            imagesRepository.save(avatar);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }

    private void deleteImageToDirektori(String avatarNameDelete) {
        Path targetLocation = this.storageLocationUserAvatar.resolve(avatarNameDelete);
        try {
            Files.delete(targetLocation);
        } catch (IOException e) {
//            throw new RuntimeException("Image not found");
            System.out.println(e.getMessage());
            return;
        }
    }

    public Page<Images> getImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return imagesRepository.findAll(pageable, true);
    }

    public String saveWebImage(MultipartFile file, String contentType) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            contentType = contentType.replaceAll("image/", ".");
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + contentType;
            Path targetLocation = this.fileStorageLocation.resolve(resultFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Images image = new Images();
            image.setImageName(resultFilename);
            image.setWebImage(true);
            imagesRepository.save(image);
            return resultFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//            System.out.println(ex.getMessage());
        }
    }

    public Resource loadWebImages(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);

            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
//            System.out.println(ex.getMessage());
        }
    }
}