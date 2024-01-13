package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.dto.PhraseUserDto;
//import com.example.learnenglish.mapper.PhraseUserMapper;
import com.example.learnenglish.model.users.PhraseUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.PhrasesAndUserRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
// Буде змінюватись
@Service
@RequiredArgsConstructor
public class PhraseUserService {
    private final TranslationPairService translationPairService;
//    private final PhraseLessonService phraseLessonService;
    private final UserService userService;
    private final PhraseUserRepository phraseUserRepository;
    private final PhraseAndUserService phraseAndUserService;
    private final PhrasesAndUserRepository phrasesAndUserRepository;
//    private final PhraseUserMapper mapper;

    public CustomResponseMessage saveNewPhraseUser(User user, PhraseUserDto phraseUserDto) {
//        PhraseUser phraseUser = mapper.toModel(phraseUserDto);
//        phraseUser.setUser(user);
//        phraseAndUserService.saveNewPhrasesAndUser(phraseUser);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public boolean duplicatePhraseUserInDB(User user, String engPhrase) {
        return phraseUserRepository.existsPhraseUserByUserAndEngPhraseIsIgnoreCase(user, engPhrase);

    }



    public Optional<?> check(DtoTranslationPair dtoTranslationPair, String roleUser) {
//        Optional<PhraseUser> translationPairOptional = phraseUserRepository.findById(dtoTranslationPair.getId());
//        DtoTranslationPair dtoTranslationPairCleared = cleaningText(dtoTranslationPair, roleUser); //!!!!!
//
//        if (translationPairOptional.isPresent() && validateTranslationPairs(dtoTranslationPairCleared)) {
//            PhraseUser phraseUserBase = translationPairOptional.get();
//            phraseUserBase.setUkrTranslate(dtoTranslationPairCleared.getUkrText());
////            phraseUserBase.setUkrTextFemale(dtoTranslationPairCleared.getUkrTextFemale());
//            phraseUserBase.setEngPhrase(dtoTranslationPairCleared.getEngText());
//            phraseUserRepository.save(phraseUserBase);
//            DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
//            dtoTranslationPairToUI.setId(phraseUserBase.getId());
//            dtoTranslationPairToUI.setUkrText(dtoTranslationPairCleared.getUkrText());
//            dtoTranslationPairToUI.setUkrTextFemale(dtoTranslationPairCleared.getUkrTextFemale());
//            dtoTranslationPairToUI.setEngText(dtoTranslationPairCleared.getEngText());
//            return Optional.of(dtoTranslationPairToUI);
//        } else return Optional.of(new CustomResponseMessage(Message.VALIDATE_TEXT_ERROR));
        return null;
    }

    public CustomResponseMessage saveTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
//        DtoTranslationPair dtoTranslationPairCleared = cleaningText(dtoTranslationPair, roleUser);  //!!!!!
//
//        if (validateTranslationPairs(dtoTranslationPairCleared)) {
//            if (!translationPairService.existsByEngTextAndUkrText(dtoTranslationPairCleared.getEngText(), dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId())) {
//                phraseUserRepository.save(convertToTranslationPair(dtoTranslationPairCleared, roleUser));
//                return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
//            } else {
//                return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);
//            }
//        } else {
//            return new CustomResponseMessage(Message.VALIDATE_TEXT_ERROR);
//        }

        return null;
    }






    private PhraseUser convertToTranslationPair(DtoTranslationPair dtoTranslationPair, String roleUser) {
//        PhraseUser phraseUser = new PhraseUser();
////        phraseUser.setLessonCounter(translationPairService.findByCountTranslationPairInLesson(dtoTranslationPair.getLessonId(), dtoTranslationPair.getUserId()) + 1);
//        phraseUser.setUkrTranslate(dtoTranslationPair.getUkrText());
////        phraseUser.setUkrTextFemale(dtoTranslationPair.getUkrTextFemale());
//        phraseUser.setEngPhrase(dtoTranslationPair.getEngText());
//        if(roleUser.equals("[ROLE_ADMIN]")){
//            if(phraseUser.getAudio() == null){
//                Audio audio = new Audio();
//                audio.setName(dtoTranslationPair.getEngText());
//                phraseUser.setAudio(audio);
//            } else {
//                phraseUser.getAudio().setName(dtoTranslationPair.getEngText());
//            }
//        }
//        phraseUser.setLesson(lessonService.getLesson(dtoTranslationPair.getLessonId()));
//        phraseUser.setUser(userService.getUserById(dtoTranslationPair.getUserId()));
//        if(roleUser.equals("[ROLE_USER]")){
//            phraseUser.setUserTranslationPair(true);
//            PhraseAndUser phraseAndUser = new PhraseAndUser();
//            phraseAndUser.setPhraseUser(phraseUser);
//            phraseAndUser.setUser(userService.getUserById(dtoTranslationPair.getUserId()));
//            phraseAndUser.setLesson(lessonService.getLesson(dtoTranslationPair.getLessonId()));
//            phraseAndUser.setRepeatable(true);
//            phrasesAndUserRepository.save(phraseAndUser);
//        }
//        return phraseUser;
        return null;
    }

    public PhraseUserDto cleaningText(PhraseUserDto phraseUserDto) {
        String ukrText = StringUtils.normalizeSpace(phraseUserDto.getUkrTranslate());
        String engText = StringUtils.normalizeSpace(phraseUserDto.getEngPhrase());
        ukrText = ukrText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
        engText = engText.replaceAll("[.,!~$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
        phraseUserDto.setUkrTranslate(ukrText);
        phraseUserDto.setEngPhrase(engText);
        return phraseUserDto;
    }

}
