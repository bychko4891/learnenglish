package com.example.learnenglish.repository;

import com.example.learnenglish.model.TranslationPair;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationPairRepository extends CrudRepository<TranslationPair, Integer> {
    @Query("SELECT e.id FROM TranslationPair e WHERE e.user = :userId AND e.lesson = :lessonId")
    List<Long> findAllIdsByUserAndLesson(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Query("SELECT e FROM TranslationPair e WHERE e.user.id = :userId AND e.lesson.id = :lessonId AND e.lessonCounter = :lessonCounter")
    TranslationPair findAllByUserAndLessonAndCounter( @Param("lessonId") Long lessonId, @Param("userId") Long userId, @Param("lessonCounter") Integer lessonCounter);

}
