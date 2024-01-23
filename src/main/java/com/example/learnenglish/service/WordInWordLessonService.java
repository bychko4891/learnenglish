package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordInWordLesson;
import com.example.learnenglish.repository.WordInWordLessonRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class WordInWordLessonService {

    private final WordInWordLessonRepository repository;

    public WordInWordLesson getWordInWordLesson(long wordInWordLessonId) {
        Optional<WordInWordLesson> wordInWordLessonOptional = repository.findById(wordInWordLessonId);
        if(wordInWordLessonOptional.isPresent()) {
            return wordInWordLessonOptional.get();
        } else throw new ObjectNotFoundException("");
    }

    @Transactional
    public Page<WordInWordLesson> wordsFromWordLesson(int page, int size, long wordLessonId) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.wordsFromWordLesson(pageable, wordLessonId);
    }

    @Transactional
    public List<WordInWordLesson> wordInWordLessonsToWordLessonAudit(List<Long> wordsId, int wordAuditCounter) {
        List<WordInWordLesson> wordInWordLessons = repository.findByIds(wordsId);
        for (int i = 0; i < wordInWordLessons.size(); i++) {
            wordInWordLessons.get(i).setTotalPage(wordAuditCounter);
        }
        return wordInWordLessons;
    }

    public WordInWordLesson getWordForWordLessonAudit(long wordInWordLessonId, int wordAuditCounter, int wordsIdListLength) {
        WordInWordLesson wordInWordLesson = getWordInWordLesson(wordInWordLessonId);
        wordInWordLesson.setTotalPage(wordAuditCounter - 1);
        int count = (int) (Math.random() * 10);
        if (count % 2 != 0 && wordsIdListLength > 2) {
            wordInWordLesson.setWordAuditSlide("slideAuditRadios");
        }
        return wordInWordLesson;
    }
}
