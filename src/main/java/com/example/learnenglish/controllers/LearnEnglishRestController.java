package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoUserWordLessonStatistic;
import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.UserService;
import com.example.learnenglish.service.UserWordLessonStatisticService;
import com.example.learnenglish.service.WordLessonService;
import com.example.learnenglish.service.WordService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class LearnEnglishRestController {
    private final WordService wordService;
    private final WordLessonService wordLessonService;
    private final HttpSession session;

    private final UserService userService;

    private final UserWordLessonStatisticService userWordLessonStatisticService;



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
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Word> wordsFromLesson = wordService.wordsFromLesson(page, size, wordLessonId);
            try {
                DtoWordToUI word = DtoWordToUI.convertToDTO(wordsFromLesson.getContent().get(0));
                word.setTotalPage(wordsFromLesson.getTotalPages());
                return ResponseEntity.ok(word);
            } catch (IndexOutOfBoundsException e) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-start")
    public ResponseEntity<List<DtoWordToUI>> wordFromLessonStartWord(@PathVariable("id") Long wordLessonId,
//                             @RequestParam(name = "size", defaultValue = "2") int size,
//                             @RequestParam(name = "page", defaultValue = "0") int page,
                                                                     Principal principal) {
        if (principal != null) {
//            if (page < 0) page = 0;
            Page<Word> wordsFromLesson = wordService.wordsFromLesson(0, 2, wordLessonId);
            List<DtoWordToUI> dtoWordToUIS = new ArrayList<>();
            try {
                for (Word arr : wordsFromLesson) {
                    dtoWordToUIS.add(DtoWordToUI.convertToDTO(arr));
                }
                return ResponseEntity.ok(dtoWordToUIS);
            } catch (IndexOutOfBoundsException e) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/word-confirm")
    public ResponseEntity<ResponseMessage> wordConfirm(@PathVariable("id") Long wordId,
                                                       @RequestParam(name = "wordConfirm") String word,
                                                       Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(wordService.confirmWord(word, wordId));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-audit-start")
    public ResponseEntity<List<DtoWordToUI>> startLessonWordAudit(@PathVariable("id") Long wordLessonId,
                                                                  Principal principal) {
        if (principal != null) {
            List<Long> wordsId = wordLessonService.wordsIdInWordLesson(wordLessonId);
            wordsId = wordLessonService.wordsIdInWordLesson(wordLessonId);
            int wordAuditCounter = (wordsId.size() > 9 && wordsId.size() < 16) ? (int) Math.ceil(wordsId.size() * 0.8) : wordsId.size() > 16 ? (int) Math.ceil(wordsId.size() * 0.6) : wordsId.size();
            Collections.shuffle(wordsId);
            List<Long> wordsIdStart = new ArrayList<>(wordsId.subList(0, 2));
            wordsId.subList(0, 2).clear();
            session.setAttribute("wordsId", wordsId);
            session.setAttribute("totalPage", wordAuditCounter);
            session.setAttribute("wordAuditCounter", wordAuditCounter - 2);
            session.setAttribute("wordLessonId", wordLessonId);
            return ResponseEntity.ok(wordService.wordsToAudit(wordsIdStart, wordAuditCounter));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-audit-next")
    public ResponseEntity<DtoWordToUI> nextWordForLessonWordAudit(@PathVariable("id") Long wordLessonId,
                                                                  Principal principal) {
        if (principal != null) {
            List<Long> wordsId = (List<Long>) session.getAttribute("wordsId"); //Додати перевірку довжини масива та на null
            int totalPage = (int) session.getAttribute("totalPage"); //Додати перевірку довжини масива та на null
            int wordAuditCounter = (int) session.getAttribute("wordAuditCounter");
            if (wordsId != null && wordsId.size() != 0 && wordAuditCounter != 0) {
                --wordAuditCounter;
                Collections.shuffle(wordsId);
                Long wordId = wordsId.get(0);
                wordsId.remove(0);
                if (wordsId.size() != 0) {
                    session.setAttribute("wordsId", wordsId);
                    session.setAttribute("wordAuditCounter", wordAuditCounter);
                } else {
                    session.removeAttribute("wordsId");
                    session.removeAttribute("wordAuditCounter");
                }
                return ResponseEntity.ok(wordService.getWordForWordLessonAudit(wordId, totalPage, wordsId.size()));
            }
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/word-lesson/{id}/word-lesson-audit-add-words")
    public ResponseEntity<List<String>> addWordsForLessonWordAudit(@PathVariable("id") Long wordLessonId,
                                                                   Principal principal) {
        if (principal != null) {
            List<Long> wordsId = (List<Long>) session.getAttribute("wordsId");
            if (wordsId.size() > 2) {
                Collections.shuffle(wordsId);
                List<Long> wordsIdRadios = new ArrayList<>(wordsId.subList(0, 3));
                List<DtoWordToUI> dtoWordToUIS = wordService.wordsToAudit(wordsIdRadios, 0);
                List<String> wordsRadios = new ArrayList<>();
                for (DtoWordToUI arr : dtoWordToUIS) {
                    wordsRadios.add(arr.getName());
                }
                return ResponseEntity.ok(wordsRadios);
            } else {
                List<String> words = Arrays.asList("keep", "see", "son", "day", "sun", "pink", "season", "spring",
                        "fall", "married", "passport", "finger", "slow", "closed", "rich", "floor", "wall", "roof");
                Collections.shuffle(words);
                List<String> wordsResponse = new ArrayList<>(words.subList(0, 3));
                return ResponseEntity.ok(wordsResponse);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word/{id}/word-audit-confirm")
    public ResponseEntity<String> wordAuditConfirm(@PathVariable("id") Long wordId,
                                                   @RequestBody DtoUserWordLessonStatistic userWordLessonStatistic,
                                                   Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            userWordLessonStatistic.setUser(user);
            userWordLessonStatisticService.saveUserWordLessonStatistic(userWordLessonStatistic);
            return ResponseEntity.ok("wordConfirm");
        }
        return ResponseEntity.notFound().build();
    }
}
