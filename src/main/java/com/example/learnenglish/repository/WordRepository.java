package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends CrudRepository<Word, Long> {
    @Query("SELECT MAX(w.id) FROM Word  w")
    Long lastId();

    @Query("SELECT w FROM Word w ORDER BY w.id ASC")
    Page<Word> findAll(Pageable pageable);

    boolean existsWordByNameEqualsIgnoreCase(String name);
    Optional<Word> findWordByNameEqualsIgnoreCase(String name);

//    @Query("SELECT w FROM Word w WHERE w.published = true AND LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%')")
//    List<Word> findWord(@Param("firstLetter") String firstLetter);

    @Query("SELECT w, wu.isRepeatable FROM Word w LEFT JOIN WordUser wu ON w.id = wu.word.id WHERE wu.user.id = :userId")
    Page<Object[]> findAll(Pageable pageable, @Param("userId")Long userId);

//    @Query("SELECT w FROM Word w WHERE w.wordInWordLesson.id = NULL AND LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%')")
//    @Query("SELECT w FROM Word w WHERE LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%') " +
//        "AND w.id NOT IN (SELECT wiwl.word.id FROM WordInWordLesson wiwl WHERE wiwl.word.id IS NOT NULL)")
//    List<Word> findWordToAdmin(@Param("firstLetter") String firstLetter);

    @Query("SELECT w FROM Word w WHERE LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%') " +
        "AND w.id NOT IN (SELECT vp.word.id FROM VocabularyPage vp WHERE vp.word.id IS NOT NULL)")
    List<Word> findWordForVocabularyPage(@Param("firstLetter") String firstLetter);

    @Query("SELECT w FROM Word w WHERE LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%')")
    List<Word> findWordForPhraseApplication(@Param("firstLetter") String firstLetter);


}
