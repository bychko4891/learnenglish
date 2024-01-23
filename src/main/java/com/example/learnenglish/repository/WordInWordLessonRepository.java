package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordInWordLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordInWordLessonRepository extends CrudRepository<WordInWordLesson, Long> {


    @Query("SELECT ww FROM WordInWordLesson ww WHERE ww.wordLesson.id = :wordLessonId ORDER BY ww.listOrder ASC")
    Page<WordInWordLesson> wordsFromWordLesson(Pageable pageable, @Param("wordLessonId")Long wordLessonId);

    @Query("SELECT wl FROM WordInWordLesson wl WHERE wl.id IN :ids")
    List<WordInWordLesson> findByIds(@Param("ids") List<Long> ids);
}
