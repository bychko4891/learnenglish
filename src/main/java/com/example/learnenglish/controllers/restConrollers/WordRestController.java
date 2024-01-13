package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.FileStorageService;
import com.example.learnenglish.service.WordService;
import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@Data
public class WordRestController {

    @Value("${file.upload-audio}")
    String audioStoreName;

    private final WordService wordService;
    private final FileStorageService fileStorageService;


    @PostMapping("/admin/word-save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> uploadAudioFiles(@RequestPart(value = "br", required = false) MultipartFile brAudio,
                                                                  @RequestPart(value = "usa", required = false) MultipartFile usaAudio,
                                                                  @RequestPart(value = "word") Word word,
                                                                  Principal principal) throws RuntimeException, IOException {
        if (principal != null) {
            try {
                Word wordDB = wordService.getWord(word.getId());
                word.setAudio(new Audio());
                if (brAudio != null) {
                    word.getAudio().setBrAudioName(fileStorageService.storeFile(brAudio, audioStoreName, word.getName()));
                    if (wordDB.getAudio().getBrAudioName() != null && !wordDB.getAudio().getBrAudioName().equals(wordDB.getAudio().getUsaAudioName()))
                        fileStorageService.deleteFileFromStorage(wordDB.getAudio().getBrAudioName(), audioStoreName);
                }
                if (usaAudio != null) {
                    word.getAudio().setUsaAudioName(fileStorageService.storeFile(usaAudio, audioStoreName, word.getName()));
                    if (wordDB.getAudio().getUsaAudioName() != null)
                        fileStorageService.deleteFileFromStorage(wordDB.getAudio().getUsaAudioName(), audioStoreName);
                }
                return ResponseEntity.ok(wordService.saveWord(wordDB, word));

            } catch (RuntimeException e) {
                Audio audio = new Audio();
                audio.setName(word.getName());
                if (brAudio != null)
                    audio.setBrAudioName(fileStorageService.storeFile(brAudio, audioStoreName, word.getName()));
                if (usaAudio != null)
                    audio.setBrAudioName(fileStorageService.storeFile(usaAudio, audioStoreName, word.getName()));
                word.setAudio(audio);
                return ResponseEntity.ok(wordService.saveNewWord(word));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/search-word")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<List<Word>> searchWordForAdmin(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (!searchTerm.isBlank() && principal != null) {
            List<Word> words = wordService.searchWordToAdminPage(searchTerm);
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/search-word/for-phrase")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<List<Word>> searchWordForPhrase(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (principal != null && !searchTerm.isBlank()) {
            List<Word> words = wordService.searchWordForPhraseApplication(searchTerm);
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.notFound().build();
    }

}
