package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findWordCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(boolean mainCategory, CategoryPage categoryPage);

    List<Category> findCategoriesByParentCategory_IdOrderByNameAsc(Long parentCategoryId);

}