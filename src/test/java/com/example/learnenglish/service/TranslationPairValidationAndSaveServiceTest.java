package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.TranslationPairUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TranslationPairValidationAndSaveServiceTest {
    @Mock
    private TranslationPairService translationPairService;
    @Mock
    private LessonService lessonService;
    @Mock
    private UserService userService;
    @Mock
    private PhraseUserRepository phraseUserRepository;
    @Mock
    private TranslationPairUserRepository translationPairUserRepository;
    @InjectMocks
    private TranslationPairValidationAndSaveService translationPairValidationAndSaveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        translationPairValidationAndSaveService = new TranslationPairValidationAndSaveService(translationPairService,
                lessonService, userService, phraseUserRepository, translationPairUserRepository);
    }

    @Test
    void check() {

    }

    @Test
    void saveTranslationPair() {
    }
}