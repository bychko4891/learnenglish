package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.Word;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class DtoWord {
    private Word word;
    private Category mainCategorySelect;
    private Category subcategorySelect;
    private Category subSubcategorySelect;
    private List<Long> translationPairsId;
    public DtoWord() {
    }
}
