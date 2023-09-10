package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoTranslationPair;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.repository.TranslationPairUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TranslationPairValidationAndSaveServiceTest {
    @Mock
    private TranslationPairService translationPairService;
    @Mock
    private LessonService lessonService;
    @Mock
    private UserService userService;
    @Mock
    private TranslationPairRepository translationPairRepository;
    @Mock
    private TranslationPairUserRepository translationPairUserRepository;
    @InjectMocks
    private TranslationPairValidationAndSaveService translationPairValidationAndSaveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        translationPairValidationAndSaveService = new TranslationPairValidationAndSaveService(translationPairService,
                lessonService, userService, translationPairRepository, translationPairUserRepository);
    }

    @Test
    void check() {

    }

    @Test
    void saveTranslationPair() {
    }
}