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
        if(count > 0){
            long lessonCounter= new Random().nextLong(1, count + 1);
            return translationPairConvertToDto(translationPairService.pairForLesson(lessonId, userId, lessonCounter));
        }
        return translationPairConvertToDto(translationPairService.pairForLesson(lessonId, userId, 0));
    }

    private DtoTranslationPairToUI translationPairConvertToDto(TranslationPair translationPair) {
        if(translationPair != null) {
        DtoTranslationPairToUI toUi = new DtoTranslationPairToUI();
        toUi.setUkrText(translationPair.getUkrText());
        toUi.setEngText(translationPair.getEngText());
        return toUi;
        }
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setUkrText("Завантажте текст будь ласка для навчання");
        return dtoTranslationPairToUI;
    }
}
