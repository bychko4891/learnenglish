package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.WordCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DtoWordsCategory {
    private WordCategory wordsCategory;
    private WordCategory mainCategorySelect;
    private WordCategory subcategorySelect;

    public DtoWordsCategory() {
    }
}
