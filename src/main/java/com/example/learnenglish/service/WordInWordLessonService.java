package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordInWordLesson;
import com.example.learnenglish.repository.WordInWordLessonRepository;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Data
public class WordInWordLessonService {

    private final WordInWordLessonRepository repository;

    public Page<WordInWordLesson> wordsFromWordLesson(int page, int size, long wordLessonId) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.wordsFromWordLesson(pageable, wordLessonId);
    }
}
