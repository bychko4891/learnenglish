package com.example.learnenglish.service;
/*
 *
 *
 */

import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.TranslationPairRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

@Service
public class TranslationPairService {
    private final EntityManager entityManager;
    private final TranslationPairRepository repository;

    public TranslationPairService(TranslationPairRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public TranslationPair findById(Integer id) {
        // ----- this code works!!!! ------- //
//        TypedQuery<TranslationPair> q = entityManager.createQuery(
//                "select tr from TranslationPair tr where tr.id = :id", TranslationPair.class);
//        q.setParameter("id", id);
//        return q.getSingleResult();
        // ----- this code works!!!! -END----//
        return repository.findById(id).get();
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
}
