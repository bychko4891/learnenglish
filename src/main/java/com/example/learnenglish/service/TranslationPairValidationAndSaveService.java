package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.responsemessage.*;
import com.example.learnenglish.model.TranslationPair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class TranslationPairValidationAndSaveService {
    private final TranslationPairService translationPairService;
    private final LessonService lessonService;
    private final UserService userService;
    private final TranslationPairRepository translationPairRepository;

    public TranslationPairValidationAndSaveService(TranslationPairService translationPairService, LessonService lessonService, UserService userService, TranslationPairRepository translationPairRepository) {
        this.translationPairService = translationPairService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.translationPairRepository = translationPairRepository;
    }

    public DtoTranslationPairToUI check(TranslationPair translationPair) {
        Optional<TranslationPair> translationPairOptional = translationPairRepository.findById(translationPair.getId());
        if (translationPairOptional.isPresent()) {
            TranslationPair translationPairBase = translationPairOptional.get();
            translationPairBase.setUkrText(translationPair.getUkrText());
            translationPairBase.setEngText(translationPair.getEngText());
            translationPairRepository.save(translationPairBase);
            DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
            dtoTranslationPairToUI.setId(translationPairBase.getId());
            dtoTranslationPairToUI.setUkrText(translationPairBase.getUkrText());
            dtoTranslationPairToUI.setEngText(translationPairBase.getEngText());
            return dtoTranslationPairToUI;
        }
        throw new RuntimeException("Error base method --> public TranslationPair check,  TranslationPairValidationAndSaveService.class");
    }

    // в if по ролі і відразу присвоїти відсутність тексту, перевірки винести в один метод
    public ResponseMessage saveTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
        String ukrText = StringUtils.normalizeSpace(dtoTranslationPair.getUkrText());
        String ukrTextFemale = roleUser.equals("[ROLE_ADMIN]") ? StringUtils.normalizeSpace(dtoTranslationPair.getUkrTextFemale()) : "Текст відсутній";
        String engText = StringUtils.normalizeSpace(dtoTranslationPair.getEngText());
        if (validateTranslationPairs(ukrText, ukrTextFemale, engText)) {
            if (!translationPairService.existsByEngTextAndUkrText(engText, dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
                dtoTranslationPair.setUkrText(ukrText);
                dtoTranslationPair.setUkrText(ukrTextFemale);
                dtoTranslationPair.setEngText(engText);
                translationPairRepository.save(convertToTranslationPair(dtoTranslationPair));
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
                return new ResponseMessage(Message.ERROR_DUPLICATE_TEXT);
            }
        } else {
            return new ResponseMessage(Message.ERRORVALIDATETEXT);
        }
//        return new ResponseMessage(Message.ERRORBASE);
    }

    private boolean validateTranslationPairs(String ukrText, String ukrTextFemale, String engText) {
        boolean check = false;
        ukrText = ukrText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        ukrTextFemale = ukrTextFemale.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        engText = engText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        if (Pattern.matches
                ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrText) &&
                Pattern.matches
                        ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                                "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrTextFemale) &&
                Pattern.matches
                        ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", engText)) {
            check = true;
        }
        return check;
    }


    private TranslationPair convertToTranslationPair(DtoTranslationPair dtoTranslationPair) {
        TranslationPair translationPair = new TranslationPair();
        translationPair.setLessonCounter(translationPairService.findByCountTranslationPairInLesson(dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId()) + 1);
        translationPair.setUkrText(dtoTranslationPair.getUkrText());
        translationPair.setUkrTextWoman(dtoTranslationPair.getUkrTextFemale());
        translationPair.setEngText(dtoTranslationPair.getEngText());
        translationPair.setAudioPath("path/no");
        translationPair.setLesson(lessonService.findById(dtoTranslationPair.getLessonId()));
        translationPair.setUser(userService.findById(dtoTranslationPair.getUserId()));
        return translationPair;
    }

}
