package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.WordLessonRepository;
import com.example.learnenglish.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordLessonServiceTest {
    @InjectMocks
    private WordLessonService wordLessonService;
    @Mock
    private WordLessonRepository wordLessonRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private WordRepository wordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wordLessonService = new WordLessonService(wordLessonRepository, categoryRepository, wordRepository);
    }

    @Test
    void getWordLessonsPage() {
        var page = 0;
        var size = 10;
        var pageable = PageRequest.of(page, size);

        Page<WordLesson> expectedPage = mock(Page.class);
        when(wordLessonRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<WordLesson> resultPage = wordLessonService.getWordLessonsPage(page, size);
        assertEquals(expectedPage, resultPage);
        verify(wordLessonRepository).findAll(pageable);
    }

    @Test
    void countWordLesson() {
        var expectedCount = 10L;
        when(wordLessonRepository.lastId()).thenReturn(expectedCount);

        var resultCount = wordLessonService.countWordLesson();

        assertEquals(expectedCount, resultCount);
        verify(wordLessonRepository).lastId();
    }

    @Test
    void getWordLesson() {
        var id = 1L;
        var expectedLesson = new WordLesson();
        expectedLesson.setId(id);
        when(wordLessonRepository.findById(id)).thenReturn(Optional.of(expectedLesson));

        var resultLesson = wordLessonService.getWordLesson(id);
        assertEquals(expectedLesson, resultLesson);
        verify(wordLessonRepository).findById(id);

    }

}