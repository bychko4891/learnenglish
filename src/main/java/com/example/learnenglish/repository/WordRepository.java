package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends CrudRepository<Word, Long> {

    @Query("SELECT w FROM Word w ORDER BY w.id ASC")
    Page<Word> findAll(Pageable pageable);

    @Query("SELECT w FROM Word w WHERE w.published = true AND LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%')")
    List<Word> findWord(@Param("firstLetter") String firstLetter);

    @Query("SELECT w, wu.isRepeatable FROM Word w LEFT JOIN WordUser wu ON w.id = wu.word.id WHERE wu.user.id = :userId")
    Page<Object[]> findAll(Pageable pageable, @Param("userId")Long userId);

    @Query("SELECT w FROM Word w WHERE w.wordLesson.id = NULL AND LOWER(w.name) LIKE CONCAT(LOWER(:firstLetter), '%')")
    List<Word> findWordToAdminPage(@Param("firstLetter") String firstLetter);
    @Query("SELECT w FROM Word w WHERE w.id IN :ids")
    List<Word> findByIds(@Param("ids") List<Long> ids);

}
