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
import com.example.learnenglish.model.PhraseUser;
import com.example.learnenglish.model.PhrasesAndUser;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.TranslationPairUserRepository;
import com.example.learnenglish.responsemessage.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;
// Буде змінюватись
@Service
@RequiredArgsConstructor
public class TranslationPairValidationAndSaveService {
    private final TranslationPairService translationPairService;
    private final LessonService lessonService;
    private final UserService userService;
    private final PhraseUserRepository phraseUserRepository;
    private final TranslationPairUserRepository translationPairUserRepository;



    public Optional<?> check(DtoTranslationPair dtoTranslationPair, String roleUser) {
        Optional<PhraseUser> translationPairOptional = phraseUserRepository.findById(dtoTranslationPair.getId());
        DtoTranslationPair dtoTranslationPairCleared = cleaningText(dtoTranslationPair, roleUser);

        if (translationPairOptional.isPresent() && validateTranslationPairs(dtoTranslationPairCleared)) {
            PhraseUser phraseUserBase = translationPairOptional.get();
            phraseUserBase.setUkrText(dtoTranslationPairCleared.getUkrText());
//            phraseUserBase.setUkrTextFemale(dtoTranslationPairCleared.getUkrTextFemale());
            phraseUserBase.setEngText(dtoTranslationPairCleared.getEngText());
            phraseUserRepository.save(phraseUserBase);
            DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
            dtoTranslationPairToUI.setId(phraseUserBase.getId());
            dtoTranslationPairToUI.setUkrText(dtoTranslationPairCleared.getUkrText());
            dtoTranslationPairToUI.setUkrTextFemale(dtoTranslationPairCleared.getUkrTextFemale());
            dtoTranslationPairToUI.setEngText(dtoTranslationPairCleared.getEngText());
            return Optional.of(dtoTranslationPairToUI);
        } else return Optional.of(new CustomResponseMessage(Message.VALIDATE_TEXT_ERROR));
    }

    public CustomResponseMessage saveTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
        DtoTranslationPair dtoTranslationPairCleared = cleaningText(dtoTranslationPair, roleUser);

        if (validateTranslationPairs(dtoTranslationPairCleared)) {
            if (!translationPairService.existsByEngTextAndUkrText(dtoTranslationPairCleared.getEngText(), dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
                phraseUserRepository.save(convertToTranslationPair(dtoTranslationPairCleared, roleUser));
                return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
            } else {
                return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);
            }
        } else {
            return new CustomResponseMessage(Message.VALIDATE_TEXT_ERROR);
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


    private PhraseUser convertToTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
        PhraseUser phraseUser = new PhraseUser();
//        phraseUser.setLessonCounter(translationPairService.findByCountTranslationPairInLesson(dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId()) + 1);
        phraseUser.setUkrText(dtoTranslationPair.getUkrText());
//        phraseUser.setUkrTextFemale(dtoTranslationPair.getUkrTextFemale());
        phraseUser.setEngText(dtoTranslationPair.getEngText());
        if(roleUser.equals("[ROLE_ADMIN]")){
            if(phraseUser.getAudio() == null){
                Audio audio = new Audio();
                audio.setName(dtoTranslationPair.getEngText());
                phraseUser.setAudio(audio);
            } else {
                phraseUser.getAudio().setName(dtoTranslationPair.getEngText());
            }
        }
        phraseUser.setLesson(lessonService.getLesson(dtoTranslationPair.getLessonId()));
        phraseUser.setUser(userService.getUserById(dtoTranslationPair.getUserId()));
        if(roleUser.equals("[ROLE_USER]")){
            phraseUser.setUserTranslationPair(true);
            PhrasesAndUser phrasesAndUser = new PhrasesAndUser();
            phrasesAndUser.setPhraseUser(phraseUser);
            phrasesAndUser.setUser(userService.getUserById(dtoTranslationPair.getUserId()));
            phrasesAndUser.setLesson(lessonService.getLesson(dtoTranslationPair.getLessonId()));
            phrasesAndUser.setRepeatable(true);
            translationPairUserRepository.save(phrasesAndUser);
        }
        return phraseUser;
    }

}
