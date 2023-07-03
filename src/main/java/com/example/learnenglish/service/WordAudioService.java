package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.FileStorageException;
import com.example.learnenglish.exception.MyFileNotFoundException;
import com.example.learnenglish.model.WordAudio;
import com.example.learnenglish.property.FileStorageProperties;
import com.example.learnenglish.repository.WordAudioRepository;
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
public class WordAudioService {
    private final Path fileStorageLocation;
    private final WordAudioRepository wordAudioRepository;

    public WordAudioService(FileStorageProperties fileStorageProperties,
                            WordAudioRepository wordAudioRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadAudio())
                .toAbsolutePath().normalize();
        this.wordAudioRepository = wordAudioRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//            System.out.println(ex.getMessage());
        }
    }

    public Page<WordAudio> getWordsAudioPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordAudioRepository.findAll(pageable);
    }

    public WordAudio getWordAudio(Long id) {
        Optional<WordAudio> wordAudioOptional = wordAudioRepository.findById(id);
        if(wordAudioOptional.isPresent()){
            return wordAudioOptional.get();
        }
        throw new RuntimeException("Error in method 'getWordAudio' class 'WordAudioService'");
    }

    public String saveAudioFile(MultipartFile brAudio, MultipartFile usaAudio, Long audioId) {
       WordAudio wordAudio = wordAudioRepository.findById(audioId).get();
        String fileName = StringUtils.cleanPath(brAudio.getOriginalFilename());
        String fileName2 = StringUtils.cleanPath(usaAudio.getOriginalFilename());
        try {
            if (fileName.contains("..") && fileName2.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            String nameWord = wordAudio.getWord().getName();
            String uuidFile = UUID.randomUUID().toString();
            String brAudioName = nameWord + "_uk_" + uuidFile + ".mp3";
            String usaAudioName = nameWord + "_usa_" + uuidFile + ".mp3";
            Path targetLocationBrAudio = this.fileStorageLocation.resolve(brAudioName);
            Path targetLocationUsaAudio = this.fileStorageLocation.resolve(usaAudioName);
            Files.copy(brAudio.getInputStream(), targetLocationBrAudio, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(brAudio.getInputStream(), targetLocationUsaAudio, StandardCopyOption.REPLACE_EXISTING);
            wordAudio.setBrAudioName(brAudioName);
            wordAudio.setUsaAudioName(usaAudioName);
            wordAudioRepository.save(wordAudio);
            return "yes";
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


}
