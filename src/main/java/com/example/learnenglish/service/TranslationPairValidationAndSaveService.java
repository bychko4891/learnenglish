package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.responsestatus.*;
import com.example.learnenglish.model.TranslationPair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TranslationPairValidationAndSaveService {
    private final TranslationPairService translationPairService;
    private final LessonService lessonService;
    private final UserService userService;

    public TranslationPairValidationAndSaveService(TranslationPairService translationPairService, LessonService lessonService, UserService userService) {
        this.translationPairService = translationPairService;
        this.lessonService = lessonService;
        this.userService = userService;
    }

    public ResponseStatus validationTranslationPair(DtoTranslationPair dtoTranslationPair) {
        String ukrText = StringUtils.normalizeSpace(dtoTranslationPair.getUkrText());
        String engText = StringUtils.normalizeSpace(dtoTranslationPair.getEngText());
        ukrText = ukrText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        engText = engText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        if (Pattern.matches
                ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrText) &&
                Pattern.matches
                        ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", engText)) {
            if (!translationPairService.existsByEngTextAndUkrText(engText, dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
                dtoTranslationPair.setUkrText(ukrText);
                dtoTranslationPair.setEngText(engText);
                return convertToTranslationPairEndSave(dtoTranslationPair);
            }
        }
        System.out.println("Error validationTranslationPair");
        return new ResponseStatus(Message.ERRORVALIDATETEXT);
    }

    private ResponseStatus convertToTranslationPairEndSave(DtoTranslationPair dtoTranslationPair) {
        TranslationPair pair = new TranslationPair();
        pair.setLessonCounter(translationPairService.findByCountTranslationPairInLesson(dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId()) + 1);
        pair.setUkrText(dtoTranslationPair.getUkrText());
        pair.setEngText(dtoTranslationPair.getEngText());
        pair.setAudioPath("path/no");
        pair.setLesson(lessonService.findById(dtoTranslationPair.getLessonId()));
        pair.setUser(userService.findById(dtoTranslationPair.getUserId()));
        translationPairService.saveTranslationPair(pair);
        return new ResponseStatus(Message.SUCCESSADDBASE);
    }
}
