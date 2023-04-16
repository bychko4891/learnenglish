package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.responsestatus.*;
import com.example.learnenglish.model.TranslationPair;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidationAndSaveTranslationPair {
    private final TranslationPairService translationPairService;
    private final LessonService lessonService;
    private final UserService userService;

    public ValidationAndSaveTranslationPair(TranslationPairService translationPairService, LessonService lessonService, UserService userService) {
        this.translationPairService = translationPairService;
        this.lessonService = lessonService;
        this.userService = userService;
    }

    public ResponseStatus validationTranslationPair(DtoTranslationPair dtoTranslationPair) {
        String ukrText = dtoTranslationPair.getUkr_text();
        String engText = dtoTranslationPair.getEng_text();
        ukrText = ukrText.trim();
        engText = engText.trim();
        ukrText = ukrText.replaceAll("\\s{2,}", "");
        engText = engText.replaceAll("\\s{2,}", "");
        ukrText = ukrText.replaceAll("\\b\\s\\B", "");
        engText = engText.replaceAll("\\b\\s\\B", "");
        if (Pattern.matches("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrText) &&
                Pattern.matches("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                        "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", engText)) {

            dtoTranslationPair.setUkr_text(ukrText);
            dtoTranslationPair.setEng_text(engText);
            return convertToTranslationPairEndSave(dtoTranslationPair);
        }
        System.out.println("Eroor validationTranslationPair");
        return new ResponseStatus(Message.ERRORADDBASE);
    }

    private ResponseStatus convertToTranslationPairEndSave(DtoTranslationPair dtoTranslationPair) {
        TranslationPair pair = new TranslationPair();
        pair.setLessonCounter(1);
        pair.setUkrText(dtoTranslationPair.getUkr_text());
        pair.setEngText(dtoTranslationPair.getEng_text());
        pair.setAudioPath("path/no");
        pair.setLesson(lessonService.findById(dtoTranslationPair.getLessonId()));
        pair.setUser(userService.findById(dtoTranslationPair.getUserId()));
        translationPairService.saveTranslationPair(pair);
        return new ResponseStatus(Message.SUCCESSADDBASE);
    }
}
