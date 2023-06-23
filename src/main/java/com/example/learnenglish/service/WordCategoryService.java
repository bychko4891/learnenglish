package com.example.learnenglish.service;

import com.example.learnenglish.repository.WordCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class WordCategoryService {
private final WordCategoryRepository wordCategoryRepository;

    public WordCategoryService(WordCategoryRepository wordCategoryRepository) {
        this.wordCategoryRepository = wordCategoryRepository;
    }
}
