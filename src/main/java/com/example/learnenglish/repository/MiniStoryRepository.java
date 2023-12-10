package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.MiniStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiniStoryRepository extends CrudRepository<MiniStory, Long> {

    @Query("SELECT w FROM MiniStory w ORDER BY w.id ASC")
    Page<MiniStory> findAll(Pageable pageable);
    @Query("SELECT w FROM MiniStory w WHERE w.category.id = :id")
    Page<MiniStory> findAllToUser(Pageable pageable, Long id);

}
