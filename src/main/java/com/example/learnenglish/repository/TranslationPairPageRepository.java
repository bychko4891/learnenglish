package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TranslationPairsPage;
import com.example.learnenglish.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationPairPageRepository extends CrudRepository<TranslationPairsPage, Long> {

    @Query("SELECT w FROM TranslationPairsPage w ORDER BY w.id ASC")
    Page<TranslationPairsPage> findAll(Pageable pageable);

}
