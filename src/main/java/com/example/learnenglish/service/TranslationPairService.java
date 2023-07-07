package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.TranslationPairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TranslationPairService {
    private final TranslationPairRepository translationPairRepository;


    public TranslationPairService(TranslationPairRepository repository) {
        this.translationPairRepository = repository;
    }

    public Long findByCountTranslationPairInLesson(long lessonId, long userId) {
        return translationPairRepository.findByCountTranslationPairInLesson(lessonId, userId);

    }

    public TranslationPair pairForLesson(long lessonId, long userId, long lessonCounter) {
        return translationPairRepository.findAllByUserAndLessonAndCounter(lessonId, userId, lessonCounter);
    }

    public boolean existsByEngTextAndUkrText(String engText, Long lessonId, Long userId) {
        return translationPairRepository.existsByEngTextAndUkrText(engText, lessonId, userId);
    }

    public DtoTranslationPairToUI getDtoTranslationPair(long lessonId, long userId, String userGender) {
        Long translationPairIdRandom = translationPairIdRandom(lessonId, userId);
        if (userId == 1) {
            return translationPairConvertToDtoApplicationText(lessonId, translationPairIdRandom, userGender);
        } else {
            return translationPairConvertToDtoUserText(lessonId, userId, translationPairIdRandom);
        }
    }


    private Long translationPairIdRandom(long lessonId, long userId) {
        long count = translationPairRepository.findByCountTranslationPairInLesson(lessonId, userId);
        if (count > 0) {
            return new Random().nextLong(1, count + 1);
        }
        return 0L;
    }

    private DtoTranslationPairToUI translationPairConvertToDtoUserText(Long lessonId, Long userId, Long translationPairRandomId) {
        TranslationPair translationPair = pairForLesson(lessonId, userId, translationPairRandomId);
        if (translationPair != null) {
            DtoTranslationPairToUI dtoTranslationPairToUI = convertToDTO(translationPair);
            int generateNumber = new Random().nextInt(1, 5);
            dtoTranslationPairToUI.setFragment("Fragment " + generateNumber);
            if (generateNumber == 4) {
                List<String> engTextList = new ArrayList<>(Arrays.asList(dtoTranslationPairToUI.getEngText().replaceAll("\\?+", "").split(" ")));
                engTextList.add("is");
                engTextList.add("a");
                Collections.shuffle(engTextList);
                dtoTranslationPairToUI.setEngTextList(engTextList);
            }
            return dtoTranslationPairToUI;
        }
        return translationPairIsNull();
    }

    private DtoTranslationPairToUI translationPairIsNull() {
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setUkrText("Завантажте текст будь ласка для навчання");
        dtoTranslationPairToUI.setEngText("Please download the text for study");
        dtoTranslationPairToUI.setFragment("Fragment 3");
        return dtoTranslationPairToUI;
    }

    private DtoTranslationPairToUI translationPairConvertToDtoApplicationText(Long lessonId, Long translationPairRandomId, String userGender) {
        TranslationPair translationPair = pairForLesson(lessonId, 1l, translationPairRandomId);
        if (translationPair != null) {
            DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
            dtoTranslationPairToUI.setId(translationPair.getId());
            if (userGender.equals("[FEMALE]")) {
                dtoTranslationPairToUI.setUkrText(translationPair.getUkrTextFemale());
            } else {
                dtoTranslationPairToUI.setUkrText(translationPair.getUkrText());
            }
            dtoTranslationPairToUI.setEngText(translationPair.getEngText());
            int generateNumber = new Random().nextInt(1, 5);
            dtoTranslationPairToUI.setFragment("Fragment " + generateNumber);
            if (generateNumber == 4) {
                List<String> engTextList = new ArrayList<>(Arrays.asList(dtoTranslationPairToUI.getEngText().replaceAll("\\?+", "").split(" ")));
                engTextList.add("is");
                engTextList.add("a");
                Collections.shuffle(engTextList);
                dtoTranslationPairToUI.setEngTextList(engTextList);
            }
            return dtoTranslationPairToUI;
        }
        return translationPairIsNull();
    }

    public Page<TranslationPair> getTranslationPairsPage(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        return translationPairRepository.findAll(pageable, userId);
    }

    public List<DtoTranslationPairToUI> searchResult(String src) {
        List<TranslationPair> list = translationPairRepository.findByFirstLetter(1l, src);
        List<DtoTranslationPairToUI> toUIList = new ArrayList<>();
        if (list.size() != 0) {
            for (TranslationPair arr : list) {
                toUIList.add(convertToDTO(arr));
            }
            return toUIList;
        }
        return toUIList;
    }

    private DtoTranslationPairToUI convertToDTO(TranslationPair translationPair) {
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setId(translationPair.getId());
        dtoTranslationPairToUI.setUkrText(translationPair.getUkrText());
        dtoTranslationPairToUI.setEngText(translationPair.getEngText());
        return dtoTranslationPairToUI;
    }
}
