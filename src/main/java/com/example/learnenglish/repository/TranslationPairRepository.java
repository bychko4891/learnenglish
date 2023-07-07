package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationPairRepository extends CrudRepository<TranslationPair, Long> {

    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM TranslationPair lt WHERE lt.user.id = :userId AND lt.lesson.id = :lessonId AND LOWER(lt.engText) = LOWER(:engText)")
    boolean existsByEngTextAndUkrText(@Param("engText") String engText, @Param("lessonId") Long lessonId, @Param("userId") Long userId);

    @Query("SELECT e.id FROM TranslationPair e WHERE e.user.id = :userId AND e.lesson.id = :lessonId")
    List<Long> findAllIdsByUserAndLesson(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Query("SELECT e FROM TranslationPair e WHERE e.user.id = :userId AND e.lesson.id = :lessonId AND e.lessonCounter = :lessonCounter")
    TranslationPair findAllByUserAndLessonAndCounter(@Param("lessonId") Long lessonId, @Param("userId") Long userId, @Param("lessonCounter") Long lessonCounter);

    @Query("SELECT COUNT(tr) FROM TranslationPair tr WHERE tr.lesson.id = :lessonId AND tr.user.id = :userId")
    Long findByCountTranslationPairInLesson(@Param("lessonId") Long lessonId, @Param("userId") Long userId);

    @Query("SELECT t FROM TranslationPair t WHERE t.user.id = :userId ORDER BY t.id ASC")
    Page<TranslationPair> findAll(Pageable pageable, Long userId);

    //    @Query("SELECT tr FROM TranslationPair tr WHERE tr.user.id = :id AND tr.engText LIKE CONCAT('%', :firstLetter, '%')")
//    List<TranslationPair> findByFirstLetter(@Param("id") Long id, @Param("firstLetter") String firstLetter);
    @Query("SELECT tr FROM TranslationPair tr WHERE tr.user.id = :id AND LOWER(tr.engText) LIKE CONCAT('%', LOWER(:firstLetter), '%')")
    List<TranslationPair> findByFirstLetter(@Param("id") Long id, @Param("firstLetter") String firstLetter);


}