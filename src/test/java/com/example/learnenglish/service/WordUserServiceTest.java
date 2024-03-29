package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.WordUserRepository;
import com.example.learnenglish.responsemessage.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class WordUserServiceTest {
    @InjectMocks
    private WordUserService wordUserService;
    @Mock
    private UserService userService;
    @Mock
    private WordService wordService;
    @Mock
    private WordUserRepository wordUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wordUserService = new WordUserService(wordService,wordUserRepository);
    }

}