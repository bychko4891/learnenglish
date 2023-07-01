package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DtoWord {
    private Word word;
    private WordCategory mainCategorySelect;
    private WordCategory subcategorySelect;
    private WordCategory subSubcategorySelect;

    public DtoWord() {
    }
}
