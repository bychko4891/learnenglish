package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.WordCategory;
import org.springframework.stereotype.Component;

@Component
public class DtoWordsCategory {
    private WordCategory wordCategory;
    private WordCategory mainCaregory;
    private WordCategory subcategory;
}
