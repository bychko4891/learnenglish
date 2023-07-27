package com.example.learnenglish.dto;

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.WordLesson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

//@Component
@Getter
@Setter
public class DtoWordLesson {

    private WordLesson wordLesson;
    private Category mainCategorySelect;
    private Category subcategorySelect;
    private Category subSubcategorySelect;
    private List<Long> wordsId;

    public DtoWordLesson() {
    }
}
