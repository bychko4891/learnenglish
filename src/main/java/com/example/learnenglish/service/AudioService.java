package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.FileStorageException;
import com.example.learnenglish.exception.MyFileNotFoundException;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.property.FileStorageProperties;
import com.example.learnenglish.repository.AudioRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// Буде змінюватись в роботу 28.09
@Service
public class AudioService {
    private final Path fileStorageLocation;
    private final AudioRepository audioRepository;

    public AudioService(FileStorageProperties fileStorageProperties,
                        AudioRepository audioRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadAudio())
                .toAbsolutePath().normalize();
        this.audioRepository = audioRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//            System.out.println(ex.getMessage());
        }
    }

    public Page<Audio> getWordsAudioPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return audioRepository.findAll(pageable);
    }

    public Audio getWordAudio(Long id) {
        Optional<Audio> audioOptional = audioRepository.findById(id);
        if (audioOptional.isPresent()) {
            return audioOptional.get();
        }
        throw new RuntimeException("Error in method 'getWordAudio' class 'AudioService'");
    }

    public CustomResponseMessage saveAudioFile(Map<String, MultipartFile> audioFiles, Long audioId) {
        Audio audio = audioRepository.findById(audioId).get();
        for (Map.Entry<String, MultipartFile> file : audioFiles.entrySet()) {
            String fileName = StringUtils.cleanPath(file.getValue().getOriginalFilename());
            try {
                if (fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                String uuidFile = UUID.randomUUID().toString();
                String audioName = audio.getName() + "_uk_" + uuidFile + ".mp3";
                if (file.getKey().equals("usaAudio")) {
                    audioName = audio.getName() + "_usa_" + uuidFile + ".mp3";
                    audio.setUsaAudioName(audioName);
                } else {
                    audio.setBrAudioName(audioName);
                }
                Path targetLocationAudio = this.fileStorageLocation.resolve(audioName);
                Files.copy(file.getValue().getInputStream(), targetLocationAudio, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        audioRepository.save(audio);
        return new CustomResponseMessage(Message.SUCCESS);
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

    private void deleteImageFromDirectory(String avatarNameDelete) {
        Path targetLocation = this.fileStorageLocation.resolve(avatarNameDelete);
        try {
            Files.delete(targetLocation);
        } catch (IOException e) {
//            throw new RuntimeException("Image not found");
            System.out.println(e.getMessage());
            return;
        }
    }


}
