package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.PhrasesAndUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PhraseAndUserServiceTest {

    @Mock
    private TranslationPairService translationPairService;
    @Mock
    private PhraseLessonService phraseLessonService;
    @Mock
    private UserService userService;
    @Mock
    private PhraseUserRepository phraseUserRepository;

    @Mock
    private PhraseAndUserService phraseAndUserService;
    @Mock
    private PhrasesAndUserRepository phrasesAndUserRepository;
    @InjectMocks
    private PhraseUserService phraseUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phraseUserService = new PhraseUserService(translationPairService,
                phraseLessonService, userService, phraseUserRepository, phraseAndUserService, phrasesAndUserRepository);
    }

    @Test
    void check() {

    }

    @Test
    void saveTranslationPair() {
    }
}