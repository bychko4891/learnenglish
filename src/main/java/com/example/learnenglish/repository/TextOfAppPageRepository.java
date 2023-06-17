package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TextOfAppPage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextOfAppPageRepository extends CrudRepository<TextOfAppPage, Long> {
    @Query("SELECT t FROM TextOfAppPage t WHERE t.pageApplication.id = :pageApplicationId")
    Optional<TextOfAppPage> searchTextOfAppPageByPageApplicationId(Long pageApplicationId);
}
