package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TranslationPairService {
    private final TranslationPairRepository translationPairRepository;

    private final UserService userService;


    public TranslationPairService(TranslationPairRepository repository,
                                  UserService userService) {
        this.translationPairRepository = repository;
        this.userService = userService;
    }

    public Long findByCountTranslationPairInLesson(long lessonId, long userId) {
        return translationPairRepository.countTranslationPairByUserIdAndLessonId(userId, lessonId);

    }

    public TranslationPair pairForLesson(long lessonId, long userId, long lessonCounter) {
        return translationPairRepository.findAllByUserAndLessonAndCounter(lessonId, userId, lessonCounter);
    }

    public boolean existsByEngTextAndUkrText(String engText, Long lessonId, Long userId) {
        return translationPairRepository.existsByEngTextAndUkrText(engText, lessonId, userId);
    }


    public DtoTranslationPairToUI getDtoTranslationPair(User user, Long lessonId,  String userGender) {
        if(!user.isUserTextInLesson()){
            Optional<TranslationPair> translationPairOptional = translationPairRepository.randomTranslationPair(1l, lessonId);
            if(translationPairOptional.isPresent()){
                return translationPairConvertToDtoApplicationText(translationPairOptional.get(), userGender);
            } else return translationPairIsNull();
        }else {
            Optional<TranslationPair> translationPairOptional = translationPairRepository.randomTranslationPairUserText(user.getId(), lessonId);
            if(translationPairOptional.isPresent()){
                return translationPairConvertToDtoUserText(translationPairOptional.get());
            } else return translationPairIsNull();
        }
    }


    private DtoTranslationPairToUI translationPairConvertToDtoUserText(TranslationPair translationPair) {
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

    private DtoTranslationPairToUI translationPairIsNull() {
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setUkrText("Нема фраз для тренування");
        dtoTranslationPairToUI.setEngText("There are no phrases for training");
        dtoTranslationPairToUI.setFragment("Fragment 3");
        return dtoTranslationPairToUI;
    }

    private DtoTranslationPairToUI translationPairConvertToDtoApplicationText(TranslationPair translationPair, String userGender) {
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

    public Page<TranslationPair> getUserTranslationPairs(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = translationPairRepository.findAll(pageable, userId);
        List<TranslationPair> translationPairs = new ArrayList<>();
        for (Object[] result : resultPage.getContent()) {
            TranslationPair translationPair = (TranslationPair) result[0];
            Boolean isRepeatable = (Boolean) result[1];
            translationPair.setRepeatable(isRepeatable);
            translationPairs.add(translationPair);
        }
        return new PageImpl<>(translationPairs, pageable, resultPage.getTotalElements());
    }

    public Page<TranslationPair> getTranslationPairsFourAdmin(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        return translationPairRepository.findAllForAdmin(pageable, userId);
    }

    public List<DtoTranslationPairToUI> searchResult(String src) {
        List<TranslationPair> list = translationPairRepository.findTranslationPair(1l, src);
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
