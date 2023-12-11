package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.users.PhraseUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.PhraseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class TranslationPairService {
    private final PhraseUserRepository phraseUserRepository;

    public Long findByCountTranslationPairInLesson(long lessonId, long userId) {
//        return phraseUserRepository.countTranslationPairByUserIdAndLessonId(userId, lessonId);

        return null;
    }

    public boolean existsByEngTextAndUkrText(String engText, Long lessonId, Long userId) {
//        return phraseUserRepository.existsByEngTextAndUkrText(engText, lessonId, userId);
        return true;
    }


    public DtoTranslationPairToUI getDtoTranslationPair(User user, Long lessonId,  String userGender) {
//        if(!user.isUserPhrasesInLesson()){
//            Optional<PhraseUser> translationPairOptional = phraseUserRepository.randomTranslationPair(1l, lessonId);
//            if(translationPairOptional.isPresent()){
//                return translationPairConvertToDtoApplicationText(translationPairOptional.get(), userGender);
//            } else return translationPairIsNull();
//        }else {
//            Optional<PhraseUser> translationPairOptional = phraseUserRepository.randomTranslationPairUserText(user.getId(), lessonId);
//            if(translationPairOptional.isPresent()){
//                return translationPairConvertToDtoUserText(translationPairOptional.get());
//            } else return translationPairIsNull();
//        }
        return null;
    }


    private DtoTranslationPairToUI translationPairConvertToDtoUserText(PhraseUser phraseUser) {
        DtoTranslationPairToUI dtoTranslationPairToUI = DtoTranslationPairToUI.convertToDTO(phraseUser);
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

    private DtoTranslationPairToUI translationPairConvertToDtoApplicationText(PhraseUser phraseUser, String userGender) {
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setId(phraseUser.getId());
        dtoTranslationPairToUI.setUkrText(phraseUser.getUkrTranslate());
        dtoTranslationPairToUI.setEngText(phraseUser.getEngPhrase());
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

    public Page<PhraseUser> getUserTranslationPairs(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = phraseUserRepository.findAll(pageable, userId);
        List<PhraseUser> phraseUsers = new ArrayList<>();
        for (Object[] result : resultPage.getContent()) {
            PhraseUser phraseUser = (PhraseUser) result[0];
            Boolean isRepeatable = (Boolean) result[1];
            phraseUser.setRepeatable(isRepeatable);
            phraseUsers.add(phraseUser);
        }
        return new PageImpl<>(phraseUsers, pageable, resultPage.getTotalElements());
    }

//    public Page<PhraseUser> getTranslationPairsFourAdmin(int page, int size, Long userId) {
//        Pageable pageable = PageRequest.of(page, size);
//        return phraseUserRepository.findAllPhraseApplicationForAdmin(pageable, userId);
//    }

    public List<DtoTranslationPairToUI> searchResult(String searchTerm) {
        List<PhraseUser> list = phraseUserRepository.findTranslationPair(1l, searchTerm);
        List<DtoTranslationPairToUI> toUIList = new ArrayList<>();
        if (list.size() != 0) {
            for (PhraseUser arr : list) {
                toUIList.add(DtoTranslationPairToUI.convertToDTO(arr));
            }
            return toUIList;
        }
        return toUIList;
    }


}
