package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.MiniStory;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.MiniStoryRepository;
import com.example.learnenglish.repository.PhraseUserRepository;
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

class MiniStoryServiceTest {

    @Mock
    private MiniStoryRepository miniStoryRepository;

    @Mock
    private PhraseUserRepository phraseUserRepository;

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private MiniStoryService miniStoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        miniStoryService = new MiniStoryService(miniStoryRepository, phraseUserRepository, categoryRepository);
    }

    @Test
    void getTranslationPairsPages() {
        Pageable pageable = PageRequest.of(0, 10);

        List<MiniStory> translationPairsPagesList = new ArrayList<>();
        Page<MiniStory> page = new PageImpl<>(translationPairsPagesList, pageable, translationPairsPagesList.size());

        when(miniStoryRepository.findAll(pageable)).thenReturn(page);

        Page<MiniStory> result = miniStoryService.getTranslationPairsPages(0, 10);

        assertEquals(page,result);
    }

    @Test
    void getTranslationPairsPagesToUser() {
        Pageable pageable = PageRequest.of(0, 10);
        var id = 1L;

        List<MiniStory> translationPairsPagesList = new ArrayList<>();
        Page<MiniStory> page = new PageImpl<>(translationPairsPagesList, pageable, translationPairsPagesList.size());

        when(miniStoryRepository.findAllToUser(pageable,id)).thenReturn(page);

        Page<MiniStory> result = miniStoryService.getTranslationPairsPagesToUser(0, 10,id);

        assertEquals(page,result);
    }

    @Test
    void countTranslationPairPages() {
        var count = 1L;

        when(miniStoryRepository.count()).thenReturn(count);

        var resCount = miniStoryService.countTranslationPairPages();

        assertEquals(count,resCount);
    }

    @Test
    void getTranslationPairsPage() {
        var expectedId = 1L;
        var expectedTranslationPairsPage = new MiniStory();

        when(miniStoryRepository.findById(expectedId)).thenReturn(Optional.of(expectedTranslationPairsPage));

        var resultTranslationPairsPage = miniStoryService.getTranslationPairsPage(expectedId);

        assertNotNull(resultTranslationPairsPage);
        assertSame(expectedTranslationPairsPage,resultTranslationPairsPage);
    }

}