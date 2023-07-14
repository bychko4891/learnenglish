package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.responsemessage.*;
import com.example.learnenglish.model.TranslationPair;
import jakarta.servlet.http.HttpSession;
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

    public TranslationPairValidationAndSaveService(TranslationPairService translationPairService,
                                                   LessonService lessonService,
                                                   UserService userService,
                                                   TranslationPairRepository translationPairRepository) {
        this.translationPairService = translationPairService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.translationPairRepository = translationPairRepository;

    }

    public Optional<?> check(DtoTranslationPair dtoTranslationPair, String roleUser) {
        Optional<TranslationPair> translationPairOptional = translationPairRepository.findById(dtoTranslationPair.getId());
        DtoTranslationPair dtoTranslationPairCleared = cleaningText(dtoTranslationPair, roleUser);

        if (translationPairOptional.isPresent() && validateTranslationPairs(dtoTranslationPairCleared)) {
            TranslationPair translationPairBase = translationPairOptional.get();
            translationPairBase.setUkrText(dtoTranslationPairCleared.getUkrText());
            translationPairBase.setUkrTextFemale(dtoTranslationPairCleared.getUkrTextFemale());
            translationPairBase.setEngText(dtoTranslationPairCleared.getEngText());
            translationPairRepository.save(translationPairBase);
            DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
            dtoTranslationPairToUI.setId(translationPairBase.getId());
            dtoTranslationPairToUI.setUkrText(dtoTranslationPairCleared.getUkrText());
            dtoTranslationPairToUI.setUkrTextFemale(dtoTranslationPairCleared.getUkrTextFemale());
            dtoTranslationPairToUI.setEngText(dtoTranslationPairCleared.getEngText());
            return Optional.of(dtoTranslationPairToUI);
        } else return Optional.of(new ResponseMessage(Message.ERRORVALIDATETEXT));
    }

    public ResponseMessage saveTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
        DtoTranslationPair dtoTranslationPairCleared = cleaningText(dtoTranslationPair, roleUser);

        if (validateTranslationPairs(dtoTranslationPairCleared)) {
            if (!translationPairService.existsByEngTextAndUkrText(dtoTranslationPairCleared.getEngText(), dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
                translationPairRepository.save(convertToTranslationPair(dtoTranslationPairCleared, roleUser));
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
                return new ResponseMessage(Message.ERROR_DUPLICATE_TEXT);
            }
        } else {
            return new ResponseMessage(Message.ERRORVALIDATETEXT);
        }
    }

    private DtoTranslationPair cleaningText(DtoTranslationPair dtoTranslationPair, String roleUser) {
        String ukrText = StringUtils.normalizeSpace(dtoTranslationPair.getUkrText());
        String ukrTextFemale = roleUser.equals("[ROLE_ADMIN]") ? StringUtils.normalizeSpace(dtoTranslationPair.getUkrTextFemale()) : "Текст відсутній";
        String engText = StringUtils.normalizeSpace(dtoTranslationPair.getEngText());
        ukrText = ukrText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
        ukrTextFemale = ukrTextFemale.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
        engText = engText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
        dtoTranslationPair.setUkrText(ukrText);
        dtoTranslationPair.setUkrTextFemale(ukrTextFemale);
        dtoTranslationPair.setEngText(engText);
        return dtoTranslationPair;
    }

    private boolean validateTranslationPairs(DtoTranslationPair dtoTranslationPair) {
        boolean check = false;

        if (Pattern.matches
                ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{4,20}\\b$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", dtoTranslationPair.getUkrText()) &&
                Pattern.matches
                        ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{4,20}\\b$)|" +
                                "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                                "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", dtoTranslationPair.getUkrTextFemale()) &&
                Pattern.matches
                        ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", dtoTranslationPair.getEngText())) {
            check = true;
        }
        return check;
    }


    private TranslationPair convertToTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
        TranslationPair translationPair = new TranslationPair();
        translationPair.setLessonCounter(translationPairService.findByCountTranslationPairInLesson(dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId()) + 1);
        translationPair.setUkrText(dtoTranslationPair.getUkrText());
        translationPair.setUkrTextFemale(dtoTranslationPair.getUkrTextFemale());
        translationPair.setEngText(dtoTranslationPair.getEngText());
        if(roleUser.equals("[ROLE_ADMIN]")){
            if(translationPair.getAudio() == null){
                Audio audio = new Audio();
                audio.setName(dtoTranslationPair.getEngText());
                translationPair.setAudio(audio);
            } else {
                translationPair.getAudio().setName(dtoTranslationPair.getEngText());
            }
        }
        translationPair.setLesson(lessonService.findById(dtoTranslationPair.getLessonId()));
        translationPair.setUser(userService.findById(dtoTranslationPair.getUserId()));
        return translationPair;
    }

}
