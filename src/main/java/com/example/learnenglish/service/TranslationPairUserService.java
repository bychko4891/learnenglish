package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PhraseUser;
import com.example.learnenglish.model.PhrasesAndUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.TranslationPairUserRepository;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// не Буде змінюватись
@Service
public class TranslationPairUserService {
    private final TranslationPairUserRepository translationPairUserRepository;
    private final PhraseUserRepository phraseUserRepository;
    private final UserRepository userRepository;

    public TranslationPairUserService(TranslationPairUserRepository translationPairUserRepository,
                                      PhraseUserRepository phraseUserRepository, UserRepository userRepository) {
        this.translationPairUserRepository = translationPairUserRepository;
        this.phraseUserRepository = phraseUserRepository;
        this.userRepository = userRepository;
    }

    public CustomResponseMessage userPlusTranslationPairs(User user, Long translationPairsId) {
        Optional<PhrasesAndUser> optionalTranslationPairUser = translationPairUserRepository.findTranslationPairUserByPhraseUser_IdAndUserId(translationPairsId, user.getId());
        if(optionalTranslationPairUser.isEmpty()){
        PhraseUser phraseUser = phraseUserRepository.findById(translationPairsId).get();
        PhrasesAndUser phrasesAndUser = new PhrasesAndUser();
        phrasesAndUser.setUser(user);
        phrasesAndUser.setPhraseUser(phraseUser);
        phrasesAndUser.setLesson(phraseUser.getLesson());
        phrasesAndUser.setRepeatable(true);
        translationPairUserRepository.save(phrasesAndUser);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
        } return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);

    }

    public CustomResponseMessage setRepetitionPhrase(Long translationPairId, Long userId, boolean isChecked) {
        Optional<PhrasesAndUser> translationPairUserOptional = translationPairUserRepository.findTranslationPairUserByPhraseUser_IdAndUserId(translationPairId, userId);
        if (translationPairUserOptional.isPresent()) {
            PhrasesAndUser phrasesAndUser = translationPairUserOptional.get();
            phrasesAndUser.setRepeatable(isChecked);
            translationPairUserRepository.save(phrasesAndUser);
            return new CustomResponseMessage(Message.SUCCESS_CHECKBOX);
        } else return new CustomResponseMessage(Message.ERROR_SERVER);
    }

    public CustomResponseMessage userPhraseRemove(Long translationPairId, User user) {
        Optional<PhrasesAndUser> translationPairUserOptional = translationPairUserRepository.findTranslationPairUserByPhraseUser_IdAndUserId(translationPairId, user.getId());
        PhrasesAndUser phrasesAndUser = translationPairUserOptional.orElseThrow();
        if (phrasesAndUser.getPhraseUser().getUser().getId().equals(user.getId())) {
            List<PhraseUser> phraseUserList = user.getPhraseUsers();
            phraseUserList.removeIf(obj -> obj.getId().equals(translationPairId));
            user.setPhraseUsers(phraseUserList);
            userRepository.save(user);
            translationPairUserRepository.delete(translationPairUserOptional.get());
            phraseUserRepository.deleteById(translationPairId);
            return new CustomResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
        }
        translationPairUserRepository.delete(phrasesAndUser);
        return new CustomResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
    }

}
