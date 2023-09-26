package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class LessonServiceTest {
    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lessonService = new LessonService(lessonRepository);
    }

    @Test
    void getLesson() {
        Lesson sampleLesson = new Lesson();
        sampleLesson.setId(1L);
        sampleLesson.setName("Заняття № 2");
        sampleLesson.setLessonInfo("<p>Опис заняття</p>");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(sampleLesson));

        Lesson result = lessonService.getLesson(1L);

        assertEquals(sampleLesson, result);
    }


    @Test
    void countLesson() {
        var longest = 1L;
        when(lessonRepository.count()).thenReturn(longest);
        var expected = lessonService.countLessons();
        assertEquals(expected, longest);
    }
}