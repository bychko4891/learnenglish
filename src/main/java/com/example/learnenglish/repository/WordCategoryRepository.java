package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.WordCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordCategoryRepository extends CrudRepository<WordCategory, Long> {
//    @Query("SELECT w FROM WordCategory w WHERE w.mainCategory = :mainCategory")
    List<WordCategory> findWordCategoriesByMainCategory(boolean mainCategory);

}
