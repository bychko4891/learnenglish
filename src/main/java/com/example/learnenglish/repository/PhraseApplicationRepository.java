package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PhraseApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhraseApplicationRepository extends JpaRepository<PhraseApplication, Long> {

    @Query("SELECT MAX(p.id) FROM PhraseApplication  p")
    Long lastId();

    @Query("SELECT pa FROM PhraseApplication pa ORDER BY pa.id ASC")
    Page<PhraseApplication> findAllPhraseApplicationForAdmin(Pageable pageable);
}
