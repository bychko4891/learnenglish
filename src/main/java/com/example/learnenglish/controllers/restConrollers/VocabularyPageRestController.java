package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.VocabularyPage;
import com.example.learnenglish.model.Image;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.service.FileStorageService;
import com.example.learnenglish.service.VocabularyPageService;
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
public class VocabularyPageRestController {

    @Value("${file.upload-vocabulary-page-image}")
    private String wordStorePath;

    private final VocabularyPageService vocabularyPageService;

    private final FileStorageService fileStorageService;

    @PostMapping("/admin/vocabulary-page/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> vocabularyPageSave(@RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                                    @RequestPart(value = "vocabularyPage") VocabularyPage vocabularyPage,
                                                                    Principal principal) throws RuntimeException, IOException {
        if (principal != null) {
            if(vocabularyPage.getWord() == null || vocabularyPage.getWord().getId() == null) {
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_REQUIRED_FIELD));
            }
            try {
                VocabularyPage vocabularyPageDB = vocabularyPageService.getVocabularyPage(vocabularyPage.getId());
                if (!vocabularyPage.getWord().getId().equals(vocabularyPageDB.getWord().getId()) && vocabularyPageService.existVocabularyPageByName(vocabularyPage.getWord().getName())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT));
                }

                vocabularyPage.setImage(new Image());
                if (imageFile != null) {
                    vocabularyPage.getImage().setImageName(fileStorageService.storeFile(imageFile, wordStorePath, vocabularyPage.getName()));
                    if (vocabularyPageDB.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(vocabularyPageDB.getImage().getImageName(), wordStorePath);
                }
                return ResponseEntity.ok(vocabularyPageService.saveVocabularyPage(vocabularyPageDB, vocabularyPage));
            } catch (RuntimeException e) {
                if (vocabularyPageService.existVocabularyPageByName(vocabularyPage.getWord().getName())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT));
                }
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, wordStorePath, vocabularyPage.getName()));
                vocabularyPage.setImage(image);
                return ResponseEntity.ok(vocabularyPageService.saveNewVocabularyPage(vocabularyPage));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/search-vocabulary-page-for-lesson")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<List<Word>> searchWordForVocabularyPage(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (!searchTerm.isBlank() && principal != null) {
//            List<Word> words = wordService.searchWordForVocabularyPage(searchTerm);
//            return ResponseEntity.ok(words);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.notFound().build();
    }

}
