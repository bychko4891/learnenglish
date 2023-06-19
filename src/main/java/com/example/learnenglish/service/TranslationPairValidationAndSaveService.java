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

    public ResponseMessage validationTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
        if (roleUser.equals("[ROLE_ADMIN]")) {
            String ukrText = StringUtils.normalizeSpace(dtoTranslationPair.getUkrText());
            String ukrTextWoman = StringUtils.normalizeSpace(dtoTranslationPair.getUkrTextWoman());
            String engText = StringUtils.normalizeSpace(dtoTranslationPair.getEngText());
            ukrText = ukrText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
            ukrTextWoman = ukrTextWoman.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
            engText = engText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
            if (Pattern.matches
                    ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                            "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrText) &&
                    Pattern.matches
                            ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                                    "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrTextWoman) &&
                    Pattern.matches
                            ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                    "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", engText)) {
                if (!translationPairService.existsByEngTextAndUkrText(engText, dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
                    dtoTranslationPair.setUkrText(ukrText);
                    dtoTranslationPair.setUkrText(ukrTextWoman);
                    dtoTranslationPair.setEngText(engText);
                    return convertToTranslationPairEndSave(dtoTranslationPair);
                }
            }
        } else {
            return validationTranslationPairUser(dtoTranslationPair);
        }
        System.out.println("Error validationTranslationPair");
        return new ResponseMessage(Message.ERRORVALIDATETEXT);
    }

    private ResponseMessage validationTranslationPairUser(DtoTranslationPair dtoTranslationPair) {
        String ukrText = StringUtils.normalizeSpace(dtoTranslationPair.getUkrText());
        String engText = StringUtils.normalizeSpace(dtoTranslationPair.getEngText());
        ukrText = ukrText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        engText = engText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();//.replaceAll("\\b\\s\\B", "")
        if (Pattern.matches
                ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrText) &&
                Pattern.matches
                        ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", engText)
                && !translationPairService.existsByEngTextAndUkrText(engText, dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
            dtoTranslationPair.setUkrText(ukrText);
            dtoTranslationPair.setUkrTextWoman("No text ");
            dtoTranslationPair.setEngText(engText);
            return convertToTranslationPairEndSave(dtoTranslationPair);
        }
        System.out.println("Error validationTranslationPair");
        return new ResponseMessage(Message.ERRORVALIDATETEXT);
    }


    private ResponseMessage convertToTranslationPairEndSave(DtoTranslationPair dtoTranslationPair) {
        TranslationPair pair = new TranslationPair();
        pair.setLessonCounter(translationPairService.findByCountTranslationPairInLesson(dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId()) + 1);
        pair.setUkrText(dtoTranslationPair.getUkrText());
        pair.setUkrTextWoman(dtoTranslationPair.getUkrTextWoman());
        pair.setEngText(dtoTranslationPair.getEngText());
        pair.setAudioPath("path/no");
        pair.setLesson(lessonService.findById(dtoTranslationPair.getLessonId()));
        pair.setUser(userService.findById(dtoTranslationPair.getUserId()));
        translationPairService.saveTranslationPair(pair);
        return new ResponseMessage(Message.SUCCESSADDBASE);
    }

}
