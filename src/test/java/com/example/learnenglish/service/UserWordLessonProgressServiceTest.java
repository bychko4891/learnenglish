//package com.example.learnenglish.service;
//
///**
// * @author: Artur Hasparian
// * Application Name: Learn English
// * Description: Unit test
// * GitHub source code: https://github.com/bychko4891/learnenglish
// */
//
//import com.example.learnenglish.model.WordLesson;
//import com.example.learnenglish.model.users.User;
//import com.example.learnenglish.model.users.UserWordLessonProgress;
//import com.example.learnenglish.repository.UserWordLessonProgressRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//class UserWordLessonProgressServiceTest {
//
//    @Mock private UserWordLessonProgressRepository userWordLessonProgressRepository;
//    @Mock private WordLessonService wordLessonService;
//    @InjectMocks private UserWordLessonProgressService userWordLessonProgressService;
//    @Captor
//    ArgumentCaptor<UserWordLessonProgress> userWordLessonProgressArgumentCaptor;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userWordLessonProgressService = new UserWordLessonProgressService(userWordLessonProgressRepository, wordLessonService);
//    }
//
//    @Test
//    void startWordLesson() {
//        var userId = 1L;
//        var wordId = 1L;
//        var start = true;
//        var user = new User();
//        var word = new WordLesson();
//        user.setId(userId);
//        word.setId(wordId);
//        var userWordLessonProgress = new UserWordLessonProgress();
//
//        when(userWordLessonProgressRepository.findUserWordLessonProgressesByUserIdAndWordLessonId(userId, wordId))
//                .thenReturn(Optional.of(userWordLessonProgress));
//        userWordLessonProgressService.startWordLesson(user,wordId,start);
//
//        verify(userWordLessonProgressRepository).save(userWordLessonProgressArgumentCaptor.capture());
//        var saveProgress = userWordLessonProgressArgumentCaptor.getValue();
//
//        assertEquals(start, saveProgress.isStartLesson());
//    }
//}