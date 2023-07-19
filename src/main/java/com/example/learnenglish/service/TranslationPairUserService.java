package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.TranslationPairUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.repository.TranslationPairUserRepository;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TranslationPairUserService {
    private final TranslationPairUserRepository translationPairUserRepository;
    private final TranslationPairRepository translationPairRepository;
    private final UserRepository userRepository;

    public TranslationPairUserService(TranslationPairUserRepository translationPairUserRepository,
                                      TranslationPairRepository translationPairRepository, UserRepository userRepository) {
        this.translationPairUserRepository = translationPairUserRepository;
        this.translationPairRepository = translationPairRepository;
        this.userRepository = userRepository;
    }

    public ResponseMessage userPlusTranslationPairs(User user, Long translationPairsId) { // додати перевірку на дублікати
        TranslationPair translationPair = translationPairRepository.findById(translationPairsId).get();
        TranslationPairUser translationPairUser = new TranslationPairUser();
        translationPairUser.setUser(user);
        translationPairUser.setTranslationPair(translationPair);
        translationPairUser.setLesson(translationPair.getLesson());
        translationPairUser.setRepeatable(true);
        translationPairUserRepository.save(translationPairUser);
        return new ResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);

    }

    public ResponseMessage setRepetitionPhrase(Long translationPairId, Long userId, boolean isChecked) {
        Optional<TranslationPairUser> translationPairUserOptional = translationPairUserRepository.findTranslationPairUserByTranslationPair_IdAndUserId(translationPairId, userId);
        if (translationPairUserOptional.isPresent()) {
            TranslationPairUser translationPairUser = translationPairUserOptional.get();
            translationPairUser.setRepeatable(isChecked);
            translationPairUserRepository.save(translationPairUser);
            return new ResponseMessage(Message.SUCCESS_CHECKBOX);
        } else return new ResponseMessage(Message.ERROR_SERVER);
    }

    public ResponseMessage userPhraseRemove(Long translationPairId, User user) {
        Optional<TranslationPairUser> translationPairUserOptional = translationPairUserRepository.findTranslationPairUserByTranslationPair_IdAndUserId(translationPairId, user.getId());
        TranslationPairUser translationPairUser = translationPairUserOptional.orElseThrow();
        if (translationPairUser.getTranslationPair().getUser().getId() == user.getId()) {
            List<TranslationPair> translationPairList = user.getTranslationPairs();
            translationPairList.removeIf(obj -> obj.getId().equals(translationPairId));
            user.setTranslationPairs(translationPairList);
            userRepository.save(user);
            translationPairUserRepository.delete(translationPairUserOptional.get());
            translationPairRepository.deleteById(translationPairId);
            return new ResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
        }
        translationPairUserRepository.delete(translationPairUser);
        return new ResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
    }

}
