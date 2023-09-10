package com.example.learnenglish.service;

/**
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
import com.example.learnenglish.responsemessage.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordUserService {
    private final UserService userService;
    private final WordService wordService;
    private final WordUserRepository wordUserRepository;


    public ResponseMessage userWordPlus(User user, Long wordId) {
        Optional<WordUser> wordUserOptional = wordUserRepository.findWordUserByUserIdAndWordId(user.getId(), wordId);
        if(wordUserOptional.isEmpty()) {
            WordUser wordUser = new WordUser();
            Word word = wordService.getWord(wordId);
            wordUser.setUser(user);
            wordUser.setWord(word);
            wordUser.setRepeatable(true);
            wordUserRepository.save(wordUser);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else return new ResponseMessage(Message.ERROR_DUPLICATE_TEXT);
    }

    public ResponseMessage userWordRemove(Long wordId, User user) {
        Optional<WordUser> wordUserOptional = wordUserRepository.findWordUserByUserIdAndWordId(user.getId(), wordId);
        WordUser wordUser = wordUserOptional.orElseThrow();
        wordUserRepository.delete(wordUser);
        return new ResponseMessage(Message.SUCCESS_REMOVE_USER_PHRASE);
    }
}
