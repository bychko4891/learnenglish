package com.example.learnenglish.validate;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoCategory;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository categoryRepository;
    public <T> T categoryIsPresentInBase(DtoCategory dtoCategory){
        Optional<Category> categoryOptional = categoryRepository.findById(dtoCategory.getMainCategorySelect().getId());
        return categoryOptional.map(category -> (T) category).orElseGet(() -> (T) Optional.empty());
    }

    public Category categoryPageIsNull(Category category){
        if (category.getCategoryPages().get(0) == null) {
            category.getCategoryPages().clear();
            category.getCategoryPages().add(CategoryPage.NO_PAGE);
            return category;
        } else return category;
    }


}
