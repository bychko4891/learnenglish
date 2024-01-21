package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.PhraseApplication;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.PhraseApplicationService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Data
public class PhraseApplicationRestController {

    private final PhraseApplicationService phraseApplicationService;


    @PostMapping("/phrase-application-save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> savePhraseApplication(@RequestBody PhraseApplication phraseApplication,
                                                                       Principal principal) {
        if (principal != null) {
            try {
                PhraseApplication phraseApplicationDB = phraseApplicationService.getPhraseApplication(phraseApplication.getId());
                return ResponseEntity.ok(phraseApplicationService.savePhraseApplication(phraseApplicationDB, phraseApplication));
            } catch (ObjectNotFoundException e) {
                return ResponseEntity.ok(phraseApplicationService.saveNewPhraseApplication(phraseApplication));
            }
//            return ResponseEntity.ok(wordLessonService.saveWordLesson(dtoWordLesson));
        }
        return ResponseEntity.notFound().build();
    }

}
