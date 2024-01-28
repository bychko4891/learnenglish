package com.example.learnenglish.controllers.restConrollers;

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.WordInWordLesson;
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.WordInWordLessonService;
import com.example.learnenglish.service.WordLessonService;
import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Data
public class WordLessonRestController {

    private final WordInWordLessonService wordInWordLessonService;

    private final WordLessonService wordLessonService;

    private final HttpSession session;

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

    @GetMapping("/word-lesson/{id}/start-lesson")
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

    @GetMapping("/word-lesson/{wordLessonId}/word-audit-start")
    public ResponseEntity<List<WordInWordLesson>> startLessonWordAudit(@PathVariable long wordLessonId, Principal principal) {
        if (principal != null) {
            List<Long> wordsId = wordLessonService.getWordInWordLessonIdsForWordLessonAudit(wordLessonId);
            int wordAuditCounter = (wordsId.size() > 9 && wordsId.size() < 16) ? (int) Math.ceil(wordsId.size() * 0.8) : wordsId.size() > 16 ? (int) Math.ceil(wordsId.size() * 0.6) : wordsId.size();
            Collections.shuffle(wordsId);
            List<Long> wordsIdStart = new ArrayList<>(wordsId.subList(0, 2));
            wordsId.subList(0, 2).clear();
            session.setAttribute("wordsId", wordsId);
            session.setAttribute("totalPage", wordAuditCounter);
            session.setAttribute("wordAuditCounter", wordAuditCounter - 2);
            session.setAttribute("wordLessonId", wordLessonId);
            return ResponseEntity.ok(wordInWordLessonService.wordInWordLessonsToWordLessonAudit(wordsIdStart, wordAuditCounter));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-audit-next")
    public ResponseEntity<DtoWordToUI> nextWordForLessonWordAudit(@PathVariable("id") long wordLessonId,
                                                                  Principal principal) {
//        if (principal != null) {
//            List<Long> wordsId = (List<Long>) session.getAttribute("wordsId"); //Додати перевірку довжини масива та на null
//            int totalPage = (int) session.getAttribute("totalPage"); //Додати перевірку довжини масива та на null
//            int wordAuditCounter = (int) session.getAttribute("wordAuditCounter");
//            if (wordsId != null && wordsId.size() != 0 && wordAuditCounter != 0) {
//                --wordAuditCounter;
//                Collections.shuffle(wordsId);
//                Long wordId = wordsId.get(0);
//                wordsId.remove(0);
//                if (wordsId.size() != 0) {
//                    session.setAttribute("wordsId", wordsId);
//                    session.setAttribute("wordAuditCounter", wordAuditCounter);
//                } else {
//                    session.removeAttribute("wordsId");
//                    session.removeAttribute("wordAuditCounter");
//                }
//                return ResponseEntity.ok(wordService.getWordForWordLessonAudit(wordId, totalPage, wordsId.size()));
//            }
//        }
        return ResponseEntity.notFound().build();
    }

}
