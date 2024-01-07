//package com.example.learnenglish.service;
//
///**
// * @author: Artur Hasparian
// * Application Name: Learn English
// * Description: Unit test
// * GitHub source code: https://github.com/bychko4891/learnenglish
// */
//
//import com.example.learnenglish.repository.UserStatisticsRepository;
//import jakarta.servlet.http.HttpSession;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//
//
//class UserStatisticsServiceTest {
//    @InjectMocks
//    private UserStatisticsService userStatisticsService;
//    @Mock
//    private LocalDateTime localDateTime;
//    @Mock
//    UserStatisticsRepository userStatisticsRepository;
//    @Mock
//    private HttpSession session;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userStatisticsService = new UserStatisticsService(userStatisticsRepository, session);
//    }
//
//    @Test
//    void trainingStatistics() {
//    }
//
//    @Test
//    void userRepetitionCount() {
//    }
//
//    @Test
//    void errorUserRepetitionCount() {
//    }
//
//    @Test
//    void trainingDays() {
//    }
//
//    @Test
//    void addTrainingDayInListMount() {
//    }
//
//    @Test
//    void timeWeeks() {
//    }
//
//    @Test
//    void setUserRepetitionsCountNow() {
//    }
//
//    @Test
//    void saveRepetitionsCountPrev() {
//    }
//}