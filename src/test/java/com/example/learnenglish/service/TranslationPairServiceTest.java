package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Integration test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.repository.TranslationPairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TranslationPairServiceTest {

    @Mock
    private TranslationPairRepository translationPairRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TranslationPairService translationPairService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        translationPairService = new TranslationPairService(translationPairRepository, userService);
    }

    @Test
    void findByCountTranslationPairInLesson() {
        var userId = 1L;
        var lessonId = 1L;

        when(translationPairRepository.countTranslationPairByUserIdAndLessonId(userId, lessonId)).thenReturn(userId);

        var result = translationPairService.findByCountTranslationPairInLesson(1L, 1L);

        assertEquals(userId,result);
    }

    @Test
    void existsByEngTextAndUkrText() {
        var expectedText = "Expected text";

        when(translationPairRepository.existsByEngTextAndUkrText(expectedText, 2L, 1L)).thenReturn(true);

        var result = translationPairService.existsByEngTextAndUkrText(expectedText, 2L, 1L);

        assertTrue(result);
    }

    @Test
    void getDtoTranslationPair() {
    }

    @Test
    void getUserTranslationPairs() {
    }

    @Test
    void getTranslationPairsFourAdmin() {
    }

    @Test
    void searchResult() {
    }
}