package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.WordLessonService;
import com.example.learnenglish.service.WordService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class LearnEnglishRestController {
    private final WordService wordService;
    private final WordLessonService wordLessonService;
    private final HttpSession session;

    @GetMapping("/search-word")
    public ResponseEntity<List<DtoWordToUI>> searchWord(@RequestParam("searchTerm") String searchTerm) {
        if (!searchTerm.isBlank()) {
            List<DtoWordToUI> wordsResult = wordService.searchWord(searchTerm);
            return ResponseEntity.ok(wordsResult);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/word-lesson/{id}/word-next")
    public ResponseEntity<DtoWordToUI> wordFromLessonNextWord(@PathVariable("id") Long wordLessonId,
                             @RequestParam(name = "size", defaultValue = "1") int size,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             Principal principal) {
        if(principal != null) {
            if (page < 0) page = 0;
            Page<Word> wordsFromLesson = wordService.wordsFromLesson(page, size, wordLessonId);
            try {
                DtoWordToUI word = DtoWordToUI.convertToDTO(wordsFromLesson.getContent().get(0));
                word.setTotalPage(wordsFromLesson.getTotalPages());
                return ResponseEntity.ok(word);
            } catch (IndexOutOfBoundsException e){
                return ResponseEntity.notFound().build();
            }
        } return ResponseEntity.notFound().build();
    }
    @GetMapping("/word-lesson/{id}/word-start")
    public ResponseEntity<List<DtoWordToUI>> wordFromLessonStartWord(@PathVariable("id") Long wordLessonId,
//                             @RequestParam(name = "size", defaultValue = "2") int size,
//                             @RequestParam(name = "page", defaultValue = "0") int page,
                             Principal principal) {
        if(principal != null) {
//            if (page < 0) page = 0;
            Page<Word> wordsFromLesson = wordService.wordsFromLesson(0, 2, wordLessonId);
            List<DtoWordToUI> dtoWordToUIS = new ArrayList<>();
            try {
                for (Word arr: wordsFromLesson) {
                    dtoWordToUIS.add(DtoWordToUI.convertToDTO(arr));
                }
                return ResponseEntity.ok(dtoWordToUIS );
            } catch (IndexOutOfBoundsException e){
                return ResponseEntity.notFound().build();
            }
        } return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/word-confirm")
    public ResponseEntity<ResponseMessage> wordConfirm(@PathVariable("id")Long wordId,
                                                       @RequestParam(name = "wordConfirm") String word,
                                                       Principal principal){
        if(principal != null){
            return ResponseEntity.ok(wordService.confirmWord(word, wordId));
        }
       return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-audit-start")
    public ResponseEntity<List<DtoWordToUI>> wordFromLessonStartWordAudit(@PathVariable("id") Long wordLessonId,
                                                                     Principal principal) {
        if(principal != null) {
            List<Long> wordsId = wordLessonService.wordsIdInWordLesson(wordLessonId);
            int counterWordAudit = wordsId.size() < 16 ? (int)Math.ceil(wordsId.size() * 0.8) : (int)Math.ceil(wordsId.size() * 0.6);
            List<Long> wordsIdStart = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                int number = (int) (Math.random() * wordsId.size() - 1); // Додати перевірку на довжину List
               wordsIdStart.add(wordsId.get(number));
               wordsId.remove(number);
            }
            session.setAttribute("wordsId",wordLessonService.wordsIdInWordLesson(wordLessonId));
            session.setAttribute("counterWordAudit",counterWordAudit -2);
            return ResponseEntity.ok(wordService.wordsToAudit(wordsIdStart));

//            Page<Word> wordsFromLesson = wordService.wordsFromLesson(page, size, wordLessonId);
//            List<DtoWordToUI> dtoWordToUIS = new ArrayList<>();
//            try {
//                for (Word arr: wordsFromLesson) {
//                    dtoWordToUIS.add(DtoWordToUI.convertToDTO(arr));
//                }
////                DtoWordToUI word = DtoWordToUI.convertToDTO(wordsFromLesson.getContent().get());
////                word.setTotalPage(wordsFromLesson.getTotalPages());
//                return ResponseEntity.ok(dtoWordToUIS );
//            } catch (IndexOutOfBoundsException e){
//                return ResponseEntity.notFound().build();
//            }
        }
        return ResponseEntity.notFound().build();
    }
}
