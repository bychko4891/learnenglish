package com.example.learnenglish.service;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Disabled
    void name() {
    }
}