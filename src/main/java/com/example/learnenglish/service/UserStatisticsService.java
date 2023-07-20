package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoUserStatisticsToUi;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserStatistics;
import com.example.learnenglish.repository.UserStatisticsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Service
@RequiredArgsConstructor
public class UserStatisticsService {
    private LocalDateTime localDateTimeNow;
    private final UserStatisticsRepository userStatisticsRepository;
    private final HttpSession session;


    public DtoUserStatisticsToUi trainingStatistics(Long userId) {
        Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
        if (userStatisticsOptional.isPresent()) {
            UserStatistics userStatistics = userStatisticsOptional.get();
            DtoUserStatisticsToUi dtoUserStatisticsToUi = new DtoUserStatisticsToUi();
            dtoUserStatisticsToUi.setStudyTimeInTwoWeeks(userStatistics.getStudyTimeInTwoWeeks());
            dtoUserStatisticsToUi.setRepetitionsCount(userStatistics.getRepetitionsCount());
            dtoUserStatisticsToUi.setRepetitionsCountPrev(userStatistics.getRepetitionsCountPrev());
            dtoUserStatisticsToUi.setRepetitionsCountNow(userStatistics.getRepetitionsCountNow());
            dtoUserStatisticsToUi.setDaysInARowCount(userStatistics.getDaysInARowCount());
            dtoUserStatisticsToUi.setErrorCount(userStatistics.getErrorCount());
            return dtoUserStatisticsToUi;
        } else {
            throw new IllegalArgumentException("Error training Days In Month ArrayList");
        }
    }


    @After("execution(* com.example.learnenglish.service.TranslationPairService.getDtoTranslationPair(..))")
    public void userRepetitionCount() {
        localDateTimeNow = LocalDateTime.now();
        Long userId = (Long) session.getAttribute("userId");
        Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
        if (userStatisticsOptional.isPresent()) {
            UserStatistics userStatistics = userStatisticsOptional.get();
            setUserRepetitionsCountNow(userStatistics); // визов метода
            addTrainingDayInListMount(userStatistics);  // визов метода
            try {
                userStatistics.setRepetitionsCount(userStatistics.getRepetitionsCount() + 1);
                userStatistics.setTrainingTimeStartEnd(localDateTimeNow);
                userStatisticsRepository.save(userStatistics);
            } catch (NullPointerException e) {
                userStatistics.setRepetitionsCount(1);
                userStatisticsRepository.save(userStatistics);
            }
        } else throw new NoResultException("Error base in method  'userRepetitionCount' UserStatisticsService class");

    }

    public void errorUserRepetitionCount(Long userId) {
        Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
        if (userStatisticsOptional.isPresent()) {
            try {
                UserStatistics userStatistics = userStatisticsOptional.get();
                userStatistics.setErrorCount(userStatistics.getErrorCount() + 1);
                userStatisticsRepository.save(userStatistics);
            } catch (NullPointerException e) {
                UserStatistics userStatistics = userStatisticsOptional.get();
                userStatistics.setErrorCount(1);
                userStatisticsRepository.save(userStatistics);
            }
        } else
            throw new NoResultException("Error base in method  'errorUserRepetitionCount' UserStatisticsService class");

    }


    public List trainingDays(Long userId) {
        Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
        if (userStatisticsOptional.isPresent()) {
            List trainingDaysInMonth = userStatisticsOptional.get().getTrainingDaysInMonth();
            return trainingDaysInMonth;
        } else {
            throw new IllegalArgumentException("Error training Days In trainingStatistics method");
        }
    }


    public void addTrainingDayInListMount(UserStatistics userStatistics) {
        List<LocalDate> localDateList = userStatistics.getTrainingDaysInMonth();
        try{
            LocalDate localDateListMount = localDateList.get(localDateList.size() - 1);
            if (localDateListMount.getMonthValue() == localDateTimeNow.getMonthValue() &&
                    localDateListMount.getDayOfMonth() != localDateTimeNow.getDayOfMonth() ||
                    localDateListMount.getMonthValue() != localDateTimeNow.getMonthValue() &&
                            localDateListMount.getDayOfMonth() != localDateTimeNow.getDayOfMonth()) {
                localDateList.add(LocalDate.now());
            }
        }catch (IndexOutOfBoundsException e){
            localDateList.add(LocalDate.now());
        }

    }


    public List timeWeeks(Long userId) {
        Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
        if (userStatisticsOptional.isPresent()) {
//        List studyTimeInTwoWeeks = userStatisticsOptional.get().getStudyTimeInTwoWeeks();
            return userStatisticsOptional.get().getStudyTimeInTwoWeeks();
        } else {
            throw new IllegalArgumentException("Error training Days In Month ArrayList");
        }
    }


    public void setUserRepetitionsCountNow(UserStatistics userStatistics) { // +
        if(userStatistics.getTrainingTimeStartEnd() == null){
          userStatistics.setTrainingTimeStartEnd(localDateTimeNow);
        }
        if ((userStatistics.getTrainingTimeStartEnd().getMonthValue() == localDateTimeNow.getMonthValue() &&
                userStatistics.getTrainingTimeStartEnd().getDayOfMonth() == localDateTimeNow.getDayOfMonth())) {
            setCountTimeInWeeksToday(userStatistics);
            try {
                userStatistics.setRepetitionsCountNow(userStatistics.getRepetitionsCountNow() + 1); // today
            } catch (NullPointerException e) {
                userStatistics.setRepetitionsCountNow(1); // today
            }
        } else {
            saveRepetitionsCountPrev(userStatistics); // new day
        }

    }

    public void saveRepetitionsCountPrev(UserStatistics userStatistics) { // +    new day
        userStatistics.setRepetitionsCountPrev(userStatistics.getRepetitionsCountNow());
        userStatistics.setRepetitionsCountNow(1);

        setCountTimeInWeeksNewDay(userStatistics); // визов метода по new day

//        userStatistics.setDaysInARowCount(1);
    }

    //today --> true | newDay --> false
    private boolean todayNewDay(LocalDateTime userLocaleDateTimeRepetition) {
        return (userLocaleDateTimeRepetition.getMonthValue() == localDateTimeNow.getMonthValue() &&
                userLocaleDateTimeRepetition.getDayOfMonth() == localDateTimeNow.getDayOfMonth());
    }


    private void setCountTimeInWeeksToday(UserStatistics userStatistics) {
        List<Integer> studyTimeInTwoWeeks = userStatistics.getStudyTimeInTwoWeeks();
        if (((localDateTimeNow.getHour() * 60) + localDateTimeNow.getMinute()) - ((userStatistics.getTrainingTimeStartEnd().getHour() * 60)
                + userStatistics.getTrainingTimeStartEnd().getMinute()) <= 5) {
            int countTimeLearnInDay = studyTimeInTwoWeeks.get(studyTimeInTwoWeeks.size() - 1);
            int timeNowInSeconds = (localDateTimeNow.getHour() * 3600) + (localDateTimeNow.getMinute() * 60) + (localDateTimeNow.getSecond());
            int timeBaseInSeconds = (userStatistics.getTrainingTimeStartEnd().getHour() * 3600) + (userStatistics.getTrainingTimeStartEnd().getMinute() * 60) + (userStatistics.getTrainingTimeStartEnd().getSecond());
            studyTimeInTwoWeeks.set(studyTimeInTwoWeeks.size() - 1, countTimeLearnInDay + ((timeNowInSeconds - timeBaseInSeconds)));
            userStatistics.setStudyTimeInTwoWeeks(studyTimeInTwoWeeks);
        } else {
            userStatistics.setTrainingTimeStartEnd(localDateTimeNow);
        }


    }

    private void setCountTimeInWeeksNewDay(UserStatistics userStatistics) {
        List<Integer> studyTimeInTwoWeeks = userStatistics.getStudyTimeInTwoWeeks();
        if (studyTimeInTwoWeeks.size() < 14) {
            studyTimeInTwoWeeks.add(0);
            userStatistics.setStudyTimeInTwoWeeks(studyTimeInTwoWeeks);
        } else {
            studyTimeInTwoWeeks.remove(0);
            studyTimeInTwoWeeks.add(0);
            userStatistics.setStudyTimeInTwoWeeks(studyTimeInTwoWeeks);
        }

    }

}