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
import org.springframework.stereotype.Service;

@Service
public class WordUserService {
    private final UserService userService;
    private final WordService wordService;
    private final WordUserRepository wordUserRepository;

    public WordUserService(UserService userService,
                           WordService wordService,
                           WordUserRepository wordUserRepository) {
        this.userService = userService;
        this.wordService = wordService;
        this.wordUserRepository = wordUserRepository;
    }

    public ResponseMessage userWordPlus(User user, Long wordId){ // доповнити перевіркою на повтор
        WordUser wordUser = new WordUser();
        Word word = wordService.getWord(wordId);
        wordUser.setUser(user);
        wordUser.setWord(word);
        wordUser.setRepeatable(true);
        wordUserRepository.save(wordUser);
        return new ResponseMessage(Message.SUCCESSADDBASE);
    }
}
