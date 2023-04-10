package com.example.learnenglish.service;

import com.example.learnenglish.model.TranslationPair;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomTranslationPairService {
    private final TranslationPairService translationPairService;

    public RandomTranslationPairService(TranslationPairService translationPairService) {
        this.translationPairService = translationPairService;
    }

    public TranslationPair translationPairRandom() {
        return translationPairService.findById(random());
    }

    private int random() {
        int count = translationPairService.countEntities();
        return new Random().nextInt(1, count + 1);
    }
}
