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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TranslationPairService {
    private final TranslationPairRepository translationPairRepository;
    private final UserService userService;


    public TranslationPairService(TranslationPairRepository repository, UserService userService) {
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


    public DtoTranslationPairToUI getDtoTranslationPair(long lessonId, long userId, String userGender) {
        Optional<TranslationPair> translationPairOptional = translationPairRepository.randomTranslationPair(userId, lessonId);
        if (translationPairOptional.isPresent()) {
            if (userId == 1) {
                return translationPairConvertToDtoApplicationText(translationPairOptional.get(), userGender);
            } else {
                return translationPairConvertToDtoUserText(translationPairOptional.get());
            }
        } else return translationPairIsNull();
    }


    private DtoTranslationPairToUI translationPairConvertToDtoUserText(TranslationPair translationPair) {
        DtoTranslationPairToUI dtoTranslationPairToUI = convertToDTO(translationPair);
//        int generateNumber = new Random().nextInt(1, 5);
        int generateNumber = 4;
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
//        int generateNumber = new Random().nextInt(1, 5);
        int generateNumber = 4;
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

    public ResponseMessage setRepetitionPhrase(Long id, boolean isChecked) {
        Optional<TranslationPair> translationPairOptional = translationPairRepository.findById(id);
        if(translationPairOptional.isPresent()){
            TranslationPair translationPair = translationPairOptional.get();
            translationPair.setRepeatable(isChecked);
            translationPairRepository.save(translationPair);
            return new ResponseMessage(Message.SUCCESS_CHECKBOX);
        } else return new ResponseMessage(Message.ERROR_SERVER);
    }
    public ResponseMessage userPlusTranslationPairs(Long userId, Long translationPairsId, String userGender) {
        Optional<TranslationPair> translationPairOptional = translationPairRepository.findById(translationPairsId);
        User user = userService.findById(userId);
        if(translationPairOptional.isPresent()){
            if(!existsByEngTextAndUkrText(translationPairOptional.get().getEngText(), translationPairOptional.get().getLesson().getId(), user.getId()) ){
            TranslationPair translationPair = translationPairOptional.get();
            TranslationPair translationPairUser = new TranslationPair();
            translationPairUser.setUser(user);
            translationPairUser.setAudio(translationPair.getAudio());
            translationPairUser.setUkrText(translationPair.getUkrText());
            if (userGender.equals("[FEMALE]")) {
                translationPairUser.setUkrText(translationPair.getUkrTextFemale());
            }
            translationPairUser.setEngText(translationPair.getEngText());
            translationPairUser.setLesson(translationPair.getLesson());
            translationPairUser.setRepeatable(true);
            translationPairRepository.save(translationPairUser);
            return new ResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            } else {
                return new ResponseMessage(Message.ERROR_DUPLICATE_TEXT);
            }
        } else return new ResponseMessage(Message.ERROR_SERVER);
    }
}
