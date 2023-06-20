package com.example.learnenglish.service;

import com.example.learnenglish.repository.WordCatalogRepository;
import org.springframework.stereotype.Service;

@Service
public class WordCatalogService {
private final WordCatalogRepository wordCatalogRepository;

    public WordCatalogService(WordCatalogRepository wordCatalogRepository) {
        this.wordCatalogRepository = wordCatalogRepository;
    }
}
