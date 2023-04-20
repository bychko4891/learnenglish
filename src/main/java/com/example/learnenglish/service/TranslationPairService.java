package com.example.learnenglish.service;
/*
 *
 *
 */

import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.TranslationPairRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class TranslationPairService {
    private final TranslationPairRepository repository;

    public TranslationPairService(TranslationPairRepository repository) {
        this.repository = repository;

    }

    public void saveTranslationPair(TranslationPair translationPair) {
        repository.save(translationPair);
    }

    public Long findByCountTranslationPairInLesson(long lessonId, long userId) {
        return repository.findByCountTranslationPairInLesson(lessonId, userId);

    }

    public TranslationPair pairForLesson(long lessonId, long userId, long lessonCounter) {
        return repository.findAllByUserAndLessonAndCounter(lessonId, userId, lessonCounter);
    }
    public boolean existsByEngTextAndUkrText (String engText, Long lessonId, Long userId){
       return  repository.existsByEngTextAndUkrText(engText, lessonId, userId);
    }
}
