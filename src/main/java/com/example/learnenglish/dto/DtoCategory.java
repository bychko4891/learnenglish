package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DtoCategory {
    private Category mainCategorySelect;
    private Category subcategorySelect;
    private Category subSubcategorySelect;

    public DtoCategory() {
    }
}
