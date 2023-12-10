package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.PhraseUser;
import com.example.learnenglish.model.PhraseAndUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.PhrasesAndUserRepository;
import com.example.learnenglish.repository.UserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// не Буде змінюватись
@Service
@AllArgsConstructor
public class PhraseAndUserService {
    private final PhrasesAndUserRepository phrasesAndUserRepository;
    private final PhraseUserRepository phraseUserRepository;
    private final UserRepository userRepository;


    public void saveNewPhrasesAndUser(PhraseUser phraseUser) {
        PhraseAndUser phraseAndUser = new PhraseAndUser();
        phraseAndUser.setPhraseUser(phraseUser);
        phraseAndUser.setUser(phraseUser.getUser());
        phrasesAndUserRepository.save(phraseAndUser);
    }


    public CustomResponseMessage userPlusTranslationPairs(User user, Long translationPairsId) {
        Optional<PhraseAndUser> optionalTranslationPairUser = phrasesAndUserRepository.findTranslationPairUserByPhraseUser_IdAndUserId(translationPairsId, user.getId());
        if(optionalTranslationPairUser.isEmpty()){
        PhraseUser phraseUser = phraseUserRepository.findById(translationPairsId).get();
        PhraseAndUser phraseAndUser = new PhraseAndUser();
        phraseAndUser.setUser(user);
        phraseAndUser.setPhraseUser(phraseUser);
        phraseAndUser.setRepeatable(true);
        phrasesAndUserRepository.save(phraseAndUser);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
        } return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);

    }

    public CustomResponseMessage setRepetitionPhrase(Long translationPairId, Long userId, boolean isChecked) {
        Optional<PhraseAndUser> translationPairUserOptional = phrasesAndUserRepository.findTranslationPairUserByPhraseUser_IdAndUserId(translationPairId, userId);
        if (translationPairUserOptional.isPresent()) {
            PhraseAndUser phraseAndUser = translationPairUserOptional.get();
            phraseAndUser.setRepeatable(isChecked);
            phrasesAndUserRepository.save(phraseAndUser);
            return new CustomResponseMessage(Message.SUCCESS_CHECKBOX);
        } else return new CustomResponseMessage(Message.ERROR_SERVER);
    }

    public CustomResponseMessage userPhraseRemove(Long translationPairId, User user) {
        Optional<PhraseAndUser> translationPairUserOptional = phrasesAndUserRepository.findTranslationPairUserByPhraseUser_IdAndUserId(translationPairId, user.getId());
        PhraseAndUser phraseAndUser = translationPairUserOptional.orElseThrow();
        if (phraseAndUser.getPhraseUser().getUser().getId().equals(user.getId())) {
            List<PhraseUser> phraseUserList = user.getPhraseUsers();
            phraseUserList.removeIf(obj -> obj.getId().equals(translationPairId));
            user.setPhraseUsers(phraseUserList);
            userRepository.save(user);
            phrasesAndUserRepository.delete(translationPairUserOptional.get());
            phraseUserRepository.deleteById(translationPairId);
            return new CustomResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
        }
        phrasesAndUserRepository.delete(phraseAndUser);
        return new CustomResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
    }

}
