package com.example.learnenglish.service;

import com.example.learnenglish.model.WordCategory;
import com.example.learnenglish.repository.WordCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordCategoryService {
private final WordCategoryRepository wordCategoryRepository;

    public WordCategoryService(WordCategoryRepository wordCategoryRepository) {
        this.wordCategoryRepository = wordCategoryRepository;
    }

    public Long countWordCategory() {
        return wordCategoryRepository.count();
    }
    public List<WordCategory> mainWordCategoryList(boolean mainCategory) {
        return wordCategoryRepository.findWordCategoriesByMainCategory(mainCategory);
    }
}
