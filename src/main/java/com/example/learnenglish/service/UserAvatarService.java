package com.example.learnenglish.service;

import com.example.learnenglish.exception.FileStorageException;
import com.example.learnenglish.exception.MyFileNotFoundException;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserAvatar;
import com.example.learnenglish.repository.UserAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.learnenglish.property.FileStorageProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.StandardCopyOption;

@Service
public class UserAvatarService {
    private final Path fileStorageLocation;
    private final UserAvatarRepository userAvatarRepository;

    @Autowired
    public UserAvatarService(FileStorageProperties fileStorageProperties, UserAvatarRepository userAvatarRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.userAvatarRepository = userAvatarRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//            System.out.println(ex.getMessage());
        }
    }


    public String storeFile(MultipartFile file, Long userId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + ".png";
            Path targetLocation = this.fileStorageLocation.resolve(resultFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            deleteImageToDirektori(userId);
            saveUserAvatar(userId, resultFilename);
            return resultFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//            System.out.println(ex.getMessage());
        }
    }

    public Resource loadFileAsResource(String fileName) {
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

    private void saveUserAvatar(Long userId, String userAvatarName) {
        Optional<UserAvatar> optionalUserAvatar = userAvatarRepository.findById(userId);
        if (optionalUserAvatar.isPresent()) {
            UserAvatar avatar = optionalUserAvatar.get();
            avatar.setAvatarName(userAvatarName);
            userAvatarRepository.save(avatar);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }
    private void deleteImageToDirektori(Long userId) throws IOException {                   //потрібно буде десь обробити на всяк випадок
        Optional<UserAvatar> optionalUserAvatar = userAvatarRepository.findById(userId);
        if (optionalUserAvatar.isPresent()) {
            UserAvatar avatar = optionalUserAvatar.get();
            if(avatar.getAvatarName() != null){
                Path targetLocation = this.fileStorageLocation.resolve(avatar.getAvatarName());
                Files.delete(targetLocation);
            }
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }

    }
    public String avatareName(Long userId){
        Optional<UserAvatar> optionalUserAvatar = userAvatarRepository.findById(userId);
        UserAvatar avatar = optionalUserAvatar.get();

        return avatar.getAvatarName();
    }
}
