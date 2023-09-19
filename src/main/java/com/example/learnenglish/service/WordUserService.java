package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */


import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordUser;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.WordUserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
// Не Буде змінюватись
@Service
@RequiredArgsConstructor
public class WordUserService {
    private final WordService wordService;
    private final WordUserRepository wordUserRepository;


    public CustomResponseMessage userWordPlus(User user, Long wordId) {
        Optional<WordUser> wordUserOptional = wordUserRepository.findWordUserByUserIdAndWordId(user.getId(), wordId);
        if(wordUserOptional.isEmpty()) {
            WordUser wordUser = new WordUser();
            Word word = wordService.getWord(wordId);
            wordUser.setUser(user);
            wordUser.setWord(word);
            wordUser.setRepeatable(true);
            wordUserRepository.save(wordUser);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);
    }

    public CustomResponseMessage userWordRemove(Long wordId, User user) {
        Optional<WordUser> wordUserOptional = wordUserRepository.findWordUserByUserIdAndWordId(user.getId(), wordId);
        WordUser wordUser = wordUserOptional.orElseThrow();
        wordUserRepository.delete(wordUser);
        return new CustomResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
    }

    public CustomResponseMessage setRepetitionWord(Long wordId, Long userId, boolean isChecked) {
        Optional<WordUser> wordPairUserOptional = wordUserRepository.findWordUserByUserIdAndWordId(userId, wordId);
        if (wordPairUserOptional.isPresent()) {
            WordUser wordUser = wordPairUserOptional.get();
            wordUser.setRepeatable(isChecked);
            wordUserRepository.save(wordUser);
            return new CustomResponseMessage(Message.SUCCESS_CHECKBOX);
        } else return new CustomResponseMessage(Message.ERROR_SERVER);
    }
}
