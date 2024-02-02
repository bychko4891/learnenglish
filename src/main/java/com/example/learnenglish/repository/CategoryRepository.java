package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT MAX(c.id) FROM Category c")
    Long lastId();

    List<Category> findCategoriesByMainCategoryOrderByNameAsc(boolean mainCategory);

    List<Category> findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(boolean mainCategory, CategoryPage categoryPage);

    List<Category> findCategoriesByMainCategoryAndCategoryPagesOrderByIdAsc(boolean mainCategory, CategoryPage categoryPage);

    List<Category> findCategoriesByParentCategory_IdOrderByNameAsc(Long parentCategoryId);

    Optional<Category> findCategoriesByUuid(String uuid);

}