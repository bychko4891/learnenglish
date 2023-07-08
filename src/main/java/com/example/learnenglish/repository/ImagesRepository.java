package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.Images;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    @Query("SELECT i FROM Images i WHERE i.webImage = :webImage ORDER BY i.id ASC")
    Page<Images> findAll(Pageable pageable, boolean webImage);
}
