package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */
import com.example.learnenglish.model.Word;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WordServiceTest {
    @InjectMocks
    private WordService wordService;
    @Mock
    private WordRepository wordRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PhraseUserRepository phraseUserRepository;
    @Mock
    private UserService userService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        wordService = new WordService(wordRepository, categoryRepository, translationPairRepository, userService);
//    }
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