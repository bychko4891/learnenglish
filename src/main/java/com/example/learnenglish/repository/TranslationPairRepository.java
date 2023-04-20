package com.example.learnenglish.repository;

import com.example.learnenglish.model.TranslationPair;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationPairRepository extends CrudRepository<TranslationPair, Integer> {
//    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM TranslationPair lt WHERE lt.user.id = :userId AND lt.lesson.id = :lessonId AND lt.engText = :engText AND lt.ukrText = :ukrText")
//@Query("SELECT CASE WHEN COUNT(lt) >  THEN true ELSE false END FROM TranslationPair lt WHERE lt.user.id = :userId AND lt.lesson.id = :lessonId AND UPPER(lt.engText) = UPPER(:engText) AND UPPER(lt.ukrText) = UPPER(:ukrText)")
//@Query("SELECT COUNT(lt) FROM TranslationPair lt WHERE lt.user.id = :userId AND lt.lesson.id = :lessonId AND UPPER(lt.engText) = UPPER(:engText) AND UPPER(lt.ukrText) = UPPER(:ukrText)")

    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM TranslationPair lt WHERE lt.user.id = :userId AND lt.lesson.id = :lessonId AND LOWER(lt.engText) = LOWER(:engText)")
//    @Query("SELECT COUNT(*) FROM TranslationPair t WHERE t.user.id = :userId AND t.lesson.id = : lessonId AND LOWER(t.engText) = LOWER(:engText) AND LOWER(t.ukrText) = LOWER(:ukrText)")
   boolean existsByEngTextAndUkrText(@Param("engText") String engText, @Param("lessonId") Long lessonId, @Param("userId") Long userId);
//    @Query("SELECT CASE WHEN COUNT(tp) > 0 THEN TRUE ELSE FALSE END FROM TranslationPair tp WHERE tp.user.id = :userId AND tp.lesson.id = :lessonId AND tp.engText = :engText AND tp.ukrText = :ukrText")
//    boolean existsByEngTextAndUkrText(@Param("engText") String engText, @Param("ukrText") String ukrText, @Param("lessonId") Long lessonId, @Param("userId") Long userId);
//    boolean existsByUserAndLessonIdAndEngTextAndUkrText(String engText, String ukrText, Long lessonId, Long user_id);
    @Query("SELECT e.id FROM TranslationPair e WHERE e.user.id = :userId AND e.lesson.id = :lessonId")
    List<Long> findAllIdsByUserAndLesson(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    @Query("SELECT e FROM TranslationPair e WHERE e.user.id = :userId AND e.lesson.id = :lessonId AND e.lessonCounter = :lessonCounter")
    TranslationPair findAllByUserAndLessonAndCounter(@Param("lessonId") Long lessonId, @Param("userId") Long userId, @Param("lessonCounter") Long lessonCounter);

    @Query("SELECT COUNT(tr) FROM TranslationPair tr WHERE tr.lesson.id = :lessonId AND tr.user.id = :userId")
    Long findByCountTranslationPairInLesson(@Param("lessonId") Long lessonId, @Param("userId") Long userId);
}