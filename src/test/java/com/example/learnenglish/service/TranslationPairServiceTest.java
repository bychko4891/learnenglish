package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.TranslationPairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        assertEquals(userId, result);
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

        User user = new User();
        long lessonId = 1L;

        TranslationPair translationPair = new TranslationPair();
        translationPair.setEngText("Test English Text");
        when(translationPairRepository.randomTranslationPairUserText(user.getId(), lessonId)).thenReturn(Optional.of(translationPair));
        DtoTranslationPairToUI dto = translationPairService.getDtoTranslationPair(user, lessonId, "[MALE]");

        assertEquals("There are no phrases for training", dto.getEngText());
    }

    @Test
    void getUserTranslationPairs() {
        var pageable = PageRequest.of(0, 10);
        long userId = 1L;

        List<Object[]> content = new ArrayList<>();
        var translationPair1 = new TranslationPair();
        translationPair1.setId(1L);
        content.add(new Object[]{translationPair1, true});
        var translationPair2 = new TranslationPair();
        translationPair2.setId(2L);
        content.add(new Object[]{translationPair2, false});

        Page<Object[]> resultPage = new PageImpl<>(content, pageable, content.size());

        when(translationPairRepository.findAll(pageable, userId)).thenReturn(resultPage);

        Page<TranslationPair> userTranslationPairs = translationPairService.getUserTranslationPairs(0, 10, userId);

        assertEquals(2, userTranslationPairs.getTotalElements());
        assertEquals(1L, userTranslationPairs.getContent().get(0).getId());
        assertEquals(true, userTranslationPairs.getContent().get(0).isRepeatable());
        assertEquals(2L, userTranslationPairs.getContent().get(1).getId());
        assertEquals(false, userTranslationPairs.getContent().get(1).isRepeatable());
    }

    @Test
    void getTranslationPairsFourAdmin() {
        Pageable pageable = PageRequest.of(0, 10);
        long userId = 1L;

        List<Object[]> content = new ArrayList<>();
        TranslationPair translationPair1 = new TranslationPair();
        translationPair1.setId(1L);
        content.add(new Object[]{translationPair1, true});
        TranslationPair translationPair2 = new TranslationPair();
        translationPair2.setId(2L);
        content.add(new Object[]{translationPair2, false});

        Page<Object[]> resultPage = new PageImpl<>(content, pageable, content.size());

        when(translationPairRepository.findAll(pageable, userId)).thenReturn(resultPage);

        Page<TranslationPair> userTranslationPairs = translationPairService.getUserTranslationPairs(0, 10, userId);

        assertEquals(2, userTranslationPairs.getTotalElements());
        assertEquals(1L, userTranslationPairs.getContent().get(0).getId());
        assertEquals(true, userTranslationPairs.getContent().get(0).isRepeatable());
        assertEquals(2L, userTranslationPairs.getContent().get(1).getId());
        assertEquals(false, userTranslationPairs.getContent().get(1).isRepeatable());
    }

    @Test
    void searchResult() {
        String searchTerm = "test";

        List<TranslationPair> translationPairs = new ArrayList<>();
        TranslationPair translationPair1 = new TranslationPair();
        translationPair1.setId(1L);
        translationPairs.add(translationPair1);
        TranslationPair translationPair2 = new TranslationPair();
        translationPair2.setId(2L);
        translationPairs.add(translationPair2);

        when(translationPairRepository.findTranslationPair(1L, searchTerm)).thenReturn(translationPairs);

        List<DtoTranslationPairToUI> result = translationPairService.searchResult(searchTerm);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }
}