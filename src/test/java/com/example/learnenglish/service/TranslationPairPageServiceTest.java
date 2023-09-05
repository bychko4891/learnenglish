package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TranslationPairsPage;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.TranslationPairPageRepository;
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

class TranslationPairPageServiceTest {

    @Mock
    private TranslationPairPageRepository translationPairPageRepository;

    @Mock
    private TranslationPairRepository translationPairRepository;

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private TranslationPairPageService translationPairPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        translationPairPageService = new TranslationPairPageService(translationPairPageRepository, translationPairRepository, categoryRepository);
    }

    @Test
    void getTranslationPairsPages() {
        Pageable pageable = PageRequest.of(0, 10);

        List<TranslationPairsPage> translationPairsPagesList = new ArrayList<>();
        Page<TranslationPairsPage> page = new PageImpl<>(translationPairsPagesList, pageable, translationPairsPagesList.size());

        when(translationPairPageRepository.findAll(pageable)).thenReturn(page);

        Page<TranslationPairsPage> result = translationPairPageService.getTranslationPairsPages(0, 10);

        assertEquals(page,result);
    }

    @Test
    void getTranslationPairsPagesToUser() {
        Pageable pageable = PageRequest.of(0, 10);
        var id = 1L;

        List<TranslationPairsPage> translationPairsPagesList = new ArrayList<>();
        Page<TranslationPairsPage> page = new PageImpl<>(translationPairsPagesList, pageable, translationPairsPagesList.size());

        when(translationPairPageRepository.findAllToUser(pageable,id)).thenReturn(page);

        Page<TranslationPairsPage> result = translationPairPageService.getTranslationPairsPagesToUser(0, 10,id);

        assertEquals(page,result);
    }

    @Test
    void countTranslationPairPages() {
        var count = 1L;

        when(translationPairPageRepository.count()).thenReturn(count);

        var resCount = translationPairPageService.countTranslationPairPages();

        assertEquals(count,resCount);
    }

    @Test
    void getTranslationPairsPage() {
        var expectedId = 1L;
        var expectedTranslationPairsPage = new TranslationPairsPage();

        when(translationPairPageRepository.findById(expectedId)).thenReturn(Optional.of(expectedTranslationPairsPage));

        var resultTranslationPairsPage = translationPairPageService.getTranslationPairsPage(expectedId);

        assertNotNull(resultTranslationPairsPage);
        assertSame(expectedTranslationPairsPage,resultTranslationPairsPage);
    }

}