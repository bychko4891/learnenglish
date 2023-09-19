package com.example.learnenglish.validate;

import com.example.learnenglish.dto.DtoCategory;
import com.example.learnenglish.model.Category;
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



}
