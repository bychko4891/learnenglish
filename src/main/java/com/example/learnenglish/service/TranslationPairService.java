package com.example.learnenglish.service;
/*
 *
 *
 */

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.TranslationPairRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TranslationPairService {
    private final TranslationPairRepository translationPairRepository;


    public TranslationPairService(TranslationPairRepository repository) {
        this.translationPairRepository = repository;

    }

    public void saveTranslationPair(TranslationPair translationPair) {
        translationPairRepository.save(translationPair);
    }

    public Long findByCountTranslationPairInLesson(long lessonId, long userId) {
        return translationPairRepository.findByCountTranslationPairInLesson(lessonId, userId);

    }

    public TranslationPair pairForLesson(long lessonId, long userId, long lessonCounter) {
        return translationPairRepository.findAllByUserAndLessonAndCounter(lessonId, userId, lessonCounter);
    }
    public boolean existsByEngTextAndUkrText (String engText, Long lessonId, Long userId){
       return  translationPairRepository.existsByEngTextAndUkrText(engText, lessonId, userId);
    }

    public DtoTranslationPairToUI translationPairRandom(long lessonId, long userId) {
        long count = translationPairRepository.findByCountTranslationPairInLesson(lessonId, userId);
        if(count > 0){
            long lessonCounter= new Random().nextLong(1, count + 1);
            return translationPairConvertToDto(pairForLesson(lessonId, userId, lessonCounter));
        }
        return translationPairConvertToDto(pairForLesson(lessonId, userId, 0));
    }

    private DtoTranslationPairToUI translationPairConvertToDto(TranslationPair translationPair) {
        if(translationPair != null) {
            DtoTranslationPairToUI toUi = new DtoTranslationPairToUI();
            toUi.setUkrText(translationPair.getUkrText());
            toUi.setEngText(translationPair.getEngText());
            int generateNumber = new Random().nextInt(1, 5);
            toUi.setFragment("Fragment " + generateNumber);
            if (generateNumber == 4) {
                List<String> engTextList = new ArrayList<>(Arrays.asList(toUi.getEngText().replaceAll("\\?+", "").split(" ")));
                engTextList.add("is");
                engTextList.add("a");
                Collections.shuffle(engTextList);
                toUi.setEngTextList(engTextList);
            }
            return toUi;
        }
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setUkrText("Завантажте текст будь ласка для навчання");
        dtoTranslationPairToUI.setEngText("Please download the text for study");
        dtoTranslationPairToUI.setFragment("Fragment 3");
        return dtoTranslationPairToUI;
    }
}
