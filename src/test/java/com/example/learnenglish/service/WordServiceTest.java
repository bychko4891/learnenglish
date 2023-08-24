package com.example.learnenglish.service;

import com.example.learnenglish.model.Word;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WordServiceTest {
    @Autowired
    private WordService wordService;
    @Mock
    private WordRepository wordRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TranslationPairRepository translationPairRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        wordService = new WordService(wordRepository, categoryRepository, translationPairRepository, userService);
    }
    @Test
    void testGetWord() {
        Word sampleWord = new Word();
        sampleWord.setId(1L);
        sampleWord.setName("Sample Word");
        when(wordRepository.findById(1L)).thenReturn(Optional.of(sampleWord));
        Word result = wordService.getWord(1L);
        assertEquals(sampleWord, result);
    }
}