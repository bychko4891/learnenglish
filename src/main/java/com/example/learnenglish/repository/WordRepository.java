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


    // пошук по першій літері
//    @Query("SELECT tr FROM TranslationPair tr WHERE SUBSTRING(tr.engText, 1, 1) = :firstLetter")
//    List<TranslationPair> findByFirstLetter(@Param("firstLetter") String firstLetter);

}
