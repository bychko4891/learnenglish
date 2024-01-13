package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.VocabularyPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyPageRepository extends JpaRepository<VocabularyPage, Long> {

    @Query("SELECT v FROM VocabularyPage v ORDER BY v.id ASC")
    Page<VocabularyPage> findAllVocabularyPage(Pageable pageable);

    @Query("SELECT MAX(v.id) FROM VocabularyPage  v")
    long lastId();
}
