package com.example.learnenglish.controllers.restConrollers;

import com.example.learnenglish.dto.DtoUserWordLessonStatistic;
import com.example.learnenglish.dto.DtoUserWordLessonStatisticToUi;
import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.*;
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

    private final PaymentWayForPayService paymentWayForPayService;



    @GetMapping("/search-word")
    public ResponseEntity<List<DtoWordToUI>> searchWord(@RequestParam("searchTerm") String searchTerm) {
        if (!searchTerm.isBlank()) {
            List<DtoWordToUI> wordsResult = wordService.searchWord(searchTerm);
            return ResponseEntity.ok(wordsResult);
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

    @PostMapping("/word-lesson/audit/{id}/word-confirm")
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

    @GetMapping("/word-lesson/{id}/audit-result")
    public ResponseEntity<DtoUserWordLessonStatisticToUi> wordAuditResult(@PathVariable("id") Long wordLessonId,

                                                                          Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(userWordLessonStatisticService.resultWordLessonAudit(user, wordLessonId));
        }
        return ResponseEntity.notFound().build();
    }
}
