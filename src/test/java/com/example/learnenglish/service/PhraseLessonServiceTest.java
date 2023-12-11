package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PhraseLesson;
import com.example.learnenglish.repository.PhraseLessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class PhraseLessonServiceTest {
    @InjectMocks
    private PhraseLessonService phraseLessonService;

    @Mock
    private PhraseLessonRepository phraseLessonRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phraseLessonService = new PhraseLessonService(phraseLessonRepository);
    }

    @Test
    void getLesson() {
        PhraseLesson samplePhraseLesson = new PhraseLesson();
        samplePhraseLesson.setId(1L);
        samplePhraseLesson.setName("Заняття № 2");
        samplePhraseLesson.setDescription("<p>Опис заняття</p>");

        when(phraseLessonRepository.findById(1L)).thenReturn(Optional.of(samplePhraseLesson));

        PhraseLesson result = phraseLessonService.getLesson(1L);

        assertEquals(samplePhraseLesson, result);
    }


    @Test
    void countLesson() {
        var longest = 1L;
        when(phraseLessonRepository.count()).thenReturn(longest);
        var expected = phraseLessonService.countLessons();
        assertEquals(expected, longest);
    }
}