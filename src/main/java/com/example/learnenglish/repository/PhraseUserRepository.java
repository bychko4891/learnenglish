package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.PhraseUser;
import com.example.learnenglish.model.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhraseUserRepository extends CrudRepository<PhraseUser, Long> {

//    Long countTranslationPairByUserIdAndLessonId(Long userId, Long lessonId);

    boolean existsPhraseUserByUserAndEngPhraseIsIgnoreCase(User user, String engPhrase);

    @Query("SELECT CASE WHEN COUNT(lt) > 0 THEN true ELSE false END FROM PhraseUser lt WHERE lt.user.id = :userId AND LOWER(lt.engPhrase) = LOWER(:engText)")
    boolean existsByEngTextAndUkrText(@Param("engText") String engText, @Param("userId") Long userId);

//    @Query("SELECT e FROM PhraseUser e WHERE e.user.id = :userId AND e.lesson.id = :lessonId AND e.lessonCounter = :lessonCounter")
//    PhraseUser findAllByUserAndLessonAndCounter(@Param("lessonId") Long lessonId, @Param("userId") Long userId, @Param("lessonCounter") Long lessonCounter);

//    @Query("SELECT t FROM TranslationPair t INNER JOIN TranslationPairUser tu ON t.id = tu.translationPair.id WHERE tu.user.id = :userId ORDER BY t.id ASC")
    @Query("SELECT t, tu.isRepeatable FROM PhraseUser t LEFT JOIN PhraseAndUser tu ON t.id = tu.phraseUser.id WHERE tu.user.id = :userId")
    Page<Object[]> findAll(Pageable pageable, @Param("userId")Long userId);
    @Query("SELECT t FROM PhraseUser t WHERE t.user.id = :userId ORDER BY t.id ASC")
    Page<PhraseUser> findAllForAdmin(Pageable pageable, @Param("userId")Long userId);

    @Query("SELECT tr FROM PhraseUser tr WHERE tr.user.id = :id AND LOWER(tr.engPhrase) LIKE CONCAT('%', LOWER(:firstLetter), '%')")
    List<PhraseUser> findTranslationPair(@Param("id") Long id, @Param("firstLetter") String firstLetter);

    @Query("SELECT tr FROM PhraseUser tr WHERE tr.id IN :ids")
    List<PhraseUser> findByIds(@Param("ids") List<Long> ids);

    @Query("SELECT t FROM PhraseUser t WHERE t.user.id = :userId ORDER BY RANDOM() LIMIT 1")
    Optional<PhraseUser> randomTranslationPair(@Param("userId") Long userId);

    @Query("SELECT t FROM PhraseUser t INNER JOIN PhraseAndUser tu ON t.id = tu.phraseUser.id WHERE tu.user.id = :userId AND tu.isRepeatable = true ORDER BY RANDOM() LIMIT 1")
    Optional<PhraseUser> randomTranslationPairUserText(@Param("userId") Long userId);

}