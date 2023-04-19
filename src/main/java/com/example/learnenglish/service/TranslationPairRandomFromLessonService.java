package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.TranslationPair;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TranslationPairRandomFromLessonService {
    private final TranslationPairService translationPairService;

    public TranslationPairRandomFromLessonService(TranslationPairService translationPairService) {
        this.translationPairService = translationPairService;
    }

    public DtoTranslationPairToUI translationPairRandom(long lessonId, long userId) {
        long count = translationPairService.findByCountTranslationPairInLesson(lessonId, userId);
        long lessonCounter= new Random().nextLong(1, count + 1);
        return translationPairConvertToDto(translationPairService.pairForLesson(lessonId, userId, lessonCounter));
    }

    private DtoTranslationPairToUI translationPairConvertToDto(TranslationPair translationPair) {
        DtoTranslationPairToUI toUi = new DtoTranslationPairToUI();
        toUi.setUkrText(translationPair.getUkrText());
        toUi.setEngText(translationPair.getEngText());
        return toUi;
    }
}
