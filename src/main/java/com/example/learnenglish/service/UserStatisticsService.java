package com.example.learnenglish.service;

import com.example.learnenglish.model.users.TrainingTimeUsersEmbeddable;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserStatistics;
import com.example.learnenglish.repository.UserStatisticsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service

public class UserStatisticsService {
    private final UserStatisticsRepository userStatisticsRepository;
    private final EntityManager entityManager;

    public UserStatisticsService(UserStatisticsRepository userStatisticsRepository, EntityManager entityManager) {
        this.userStatisticsRepository = userStatisticsRepository;
        this.entityManager = entityManager;
    }

    public List trainingDays(Long userId) {
        Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
        if (userStatisticsOptional.isPresent()) {
            List trainingDaysInMonth = userStatisticsOptional.get().getTrainingDaysInMonth();
            return trainingDaysInMonth;
        } else {
            throw new IllegalArgumentException("Error training Days In Month ArrayList");
        }
    }

    @Transactional
    public void addTrainingDayInList(User user) {
//        select count(training_day) from training_days_mount where user_statistics_id = '1';
        String query = "SELECT COUNT(training_day) FROM training_days_mount WHERE user_statistics_id = :id";
        Long countDays = (Long) entityManager.createNativeQuery(query)
                .setParameter("id", user.getId())
                .getSingleResult();
        LocalDate date = LocalDate.now();
        if (countDays == 0) {
            UserStatistics st = user.getStatistics();
            st.setTrainingDaysInMonth(Arrays.asList(date));
            userStatisticsRepository.save(st);
        } else {
//            try {
//                String querySearch = "SELECT training_day FROM training_days_mount WHERE user_statistics_id = :id AND training_day = :day";
//                entityManager.createNativeQuery(querySearch)
//                        .setParameter("id", user.getId())
//                        .setParameter("day", date)
//                        .getSingleResult();
//            } catch (NoResultException e) {
//               String queryAdd = "INSERT INTO training_days_mount (user_statistics_id, training_day) VALUES (:stId, :date)";
            String queryAdd = "INSERT INTO training_days_mount (user_statistics_id, training_day) \n" +
                    "SELECT :stId, :date \n" +
                    "WHERE NOT EXISTS (SELECT 1 FROM training_days_mount WHERE user_statistics_id = :stId AND training_day = :date)";
            entityManager.createNativeQuery(queryAdd)
                    .setParameter("stId", user.getId())
                    .setParameter("date", date)
                    .executeUpdate();
//                UserStatistics st = user.getStatistics();
//                st = entityManager.find(UserStatistics.class, st.getId());
//                st.getTrainingDaysInMonth().add(LocalDate.now());
//                entityManager.flush();
//            }flush()add(LocalDate.now())
        }
    }

    @Transactional
    public void learnUserTime(Long userId, LocalDateTime localDateTimeNow) {
        String queryTimeStart = "SELECT training_time_start_end FROM user_statistics WHERE id = :userId";
        try {
            java.sql.Timestamp timestamp = (java.sql.Timestamp) entityManager.createNativeQuery(queryTimeStart)
                    .setParameter("userId", userId)
                    .getSingleResult();
            LocalDateTime localDateTimeBase = timestamp.toLocalDateTime();
            if (localDateTimeBase.getMonthValue() == localDateTimeNow.getMonthValue() && localDateTimeBase.getDayOfMonth() == localDateTimeNow.getDayOfMonth()) {
                if (((localDateTimeNow.getHour() * 60) + localDateTimeNow.getMinute()) - ((localDateTimeBase.getHour() * 60) + localDateTimeBase.getMinute()) <= 5) {
                    String q = "UPDATE user_statistics SET training_time_start_end = :d WHERE id = :id";
                    entityManager.createNativeQuery(q)
                            .setParameter("d", localDateTimeNow)
                            .setParameter("id", userId)
                            .executeUpdate();
                    countTimeInWeeks(userId, localDateTimeNow, localDateTimeBase, "today");
                } else {
                    String q = "UPDATE user_statistics SET training_time_start_end = :d WHERE id = :id";
                    entityManager.createNativeQuery(q)
                            .setParameter("d", localDateTimeNow)
                            .setParameter("id", userId)
                            .executeUpdate();
                }
            } else {
                String q = "UPDATE user_statistics SET training_time_start_end = :d WHERE id = :id";
                entityManager.createNativeQuery(q)
                        .setParameter("d", localDateTimeNow)
                        .setParameter("id", userId)
                        .executeUpdate();
                countTimeInWeeks(userId, localDateTimeNow, localDateTimeBase, "new day");
            }
        } catch (NullPointerException | NoResultException e) {
            String q = "UPDATE user_statistics SET training_time_start_end = :d WHERE id = :userId";
            entityManager.createNativeQuery(q)
                    .setParameter("d", localDateTimeNow)
                    .setParameter("userId", userId)
                    .executeUpdate();
            createStudyTimeInTwoWeeks(userId);
        }
    }

    @Transactional
    public void createStudyTimeInTwoWeeks(Long userId) {
        try {
            String queryTimeStart = "SELECT * FROM study_time_in_two_weeks WHERE user_statistics_id = :userId";
            entityManager.createNativeQuery(queryTimeStart)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            String q = "INSERT INTO study_time_in_two_weeks (user_statistics_id, amount_of_time_per_day) VALUES (:id, :a)";
            entityManager.createNativeQuery(q)
                    .setParameter("id", userId)
                    .setParameter("a", 0)
                    .executeUpdate();

        }
    }


    public void countTimeInWeeks(Long userId, LocalDateTime localDateTimeNow, LocalDateTime localDateTimeBase, String day) {
            Optional<UserStatistics> userStatisticsOptional = userStatisticsRepository.findById(userId);
            if (userStatisticsOptional.isPresent() && day.equals("today")) {
                UserStatistics userStatistics = userStatisticsOptional.get();
                List<Integer> studyTimeInTwoWeeks = userStatisticsOptional.get().getStudyTimeInTwoWeeks();
                int countTimeLearnInDay = studyTimeInTwoWeeks.get(studyTimeInTwoWeeks.size()-1);
                double timeNowInSeconds = (localDateTimeNow.getHour()*3600) + (localDateTimeNow.getMinute()*60) + (localDateTimeNow.getSecond());
                double timeBaseInSeconds = (localDateTimeBase.getHour()*3600) + (localDateTimeBase.getMinute()*60) + (localDateTimeBase.getSecond());
                double result = Math.ceil(timeNowInSeconds - timeBaseInSeconds) / 60;
                System.out.println(result);
                studyTimeInTwoWeeks.set(studyTimeInTwoWeeks.size() - 1, countTimeLearnInDay + (int)(Math.ceil((timeNowInSeconds - timeBaseInSeconds) / 60)));
                userStatistics.setStudyTimeInTwoWeeks(studyTimeInTwoWeeks);
                userStatisticsRepository.save(userStatistics);
            } else if (userStatisticsOptional.isPresent() && day.equals("new day")) {
                UserStatistics userStatistics = userStatisticsOptional.get();
                List<Integer> studyTimeInTwoWeeks = userStatisticsOptional.get().getStudyTimeInTwoWeeks();
                if(studyTimeInTwoWeeks.size() < 14){
                    studyTimeInTwoWeeks.add(0);
                    userStatistics.setStudyTimeInTwoWeeks(studyTimeInTwoWeeks);
                    userStatisticsRepository.save(userStatistics);
                } else {
                    studyTimeInTwoWeeks.remove(0);
                    studyTimeInTwoWeeks.add(0);
                    userStatistics.setStudyTimeInTwoWeeks(studyTimeInTwoWeeks);
                    userStatisticsRepository.save(userStatistics);
                }

            }else System.out.println("Error -> public void countTimeInWeeks  ");

    }


    @Transactional
    public void saveTrainingUserTime() {
        String query = "INSERT INTO training_days_mount (user_statistics_id, training_day) \\n\" +\n" +
                "                       \"SELECT :stId, :date \\n\" +\n" +
                "                       \"WHERE NOT EXISTS (SELECT 1 FROM training_days_mount WHERE user_statistics_id = :stId AND training_day = :date)";
    }

}
