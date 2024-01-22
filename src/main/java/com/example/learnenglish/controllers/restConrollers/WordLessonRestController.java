package com.example.learnenglish.controllers.restConrollers;

import com.example.learnenglish.model.WordInWordLesson;
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.WordInWordLessonService;
import com.example.learnenglish.service.WordLessonService;
import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Data
public class WordLessonRestController {

    private final WordInWordLessonService wordInWordLessonService;

    private final WordLessonService wordLessonService;

    @PostMapping("/admin/word-lesson/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> saveWordLesson(@RequestBody WordLesson wordLesson, Principal principal) {
        if (principal != null) {
            try {
                WordLesson wordLessonDB = wordLessonService.getWordLesson(wordLesson.getId());
                return ResponseEntity.ok(wordLessonService.saveWordLesson(wordLessonDB, wordLesson));
            } catch (RuntimeException e) {
                return ResponseEntity.ok(wordLessonService.saveNewWordLesson(wordLesson));
            }

        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-start")
    @JsonView(JsonViews.ViewWordForWordLesson.class)
    public ResponseEntity<List<WordInWordLesson>> startWordLessonGetFirstTwoWords(@PathVariable("id") long wordLessonId, Principal principal) {
        if (principal != null) {
            Page<WordInWordLesson> wordsFromLesson = wordInWordLessonService.wordsFromWordLesson(0, 2, wordLessonId);
            List<WordInWordLesson> words = wordsFromLesson.getContent();
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-next")
    @JsonView(JsonViews.ViewWordForWordLesson.class)
    public ResponseEntity<WordInWordLesson> getNextWordsFromWordLesson(@PathVariable("id") long wordLessonId,
                                                                       @RequestParam(name = "size", defaultValue = "1") int size,
                                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                                       Principal principal) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<WordInWordLesson> wordsFromLesson = wordInWordLessonService.wordsFromWordLesson(page, size, wordLessonId);
            List<WordInWordLesson> words = wordsFromLesson.getContent();
            WordInWordLesson word = words.get(0);
            word.setTotalPage(wordsFromLesson.getTotalPages());
            return ResponseEntity.ok(word);
        }
        return ResponseEntity.notFound().build();
    }


}
