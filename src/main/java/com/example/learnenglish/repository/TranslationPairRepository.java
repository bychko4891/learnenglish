package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TranslationPair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationPairRepository extends CrudRepository<TranslationPair, Long> {
    Long countTranslationPairByUserIdAndLessonId(Long userId, Long lessonId);

    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM TranslationPair lt WHERE lt.user.id = :userId AND lt.lesson.id = :lessonId AND LOWER(lt.engText) = LOWER(:engText)")
    boolean existsByEngTextAndUkrText(@Param("engText") String engText, @Param("lessonId") Long lessonId, @Param("userId") Long userId);

    @Query("SELECT e FROM TranslationPair e WHERE e.user.id = :userId AND e.lesson.id = :lessonId AND e.lessonCounter = :lessonCounter")
    TranslationPair findAllByUserAndLessonAndCounter(@Param("lessonId") Long lessonId, @Param("userId") Long userId, @Param("lessonCounter") Long lessonCounter);

    @Query("SELECT t FROM TranslationPair t WHERE t.user.id = :userId ORDER BY t.id ASC")
    Page<TranslationPair> findAll(Pageable pageable, Long userId);

    @Query("SELECT tr FROM TranslationPair tr WHERE tr.user.id = :id AND LOWER(tr.engText) LIKE CONCAT('%', LOWER(:firstLetter), '%')")
    List<TranslationPair> findByFirstLetter(@Param("id") Long id, @Param("firstLetter") String firstLetter);

    @Query("SELECT tr FROM TranslationPair tr WHERE tr.id IN :ids")
    List<TranslationPair> findByIds(@Param("ids") List<Long> ids);

    @Query("SELECT t FROM TranslationPair t WHERE t.user.id = :userId AND t.lesson.id = :lessonId AND t.isRepeatable = true ORDER BY RANDOM() LIMIT 1")
    Optional<TranslationPair> randomTranslationPair(@Param("userId")Long userId, @Param("lessonId") Long lessonId);

}