package com.example.learnenglish.service;

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserStatistics;
import com.example.learnenglish.repository.UserStatisticsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List trainingDays(Long userId){
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
            try {
                String querySearch = "SELECT training_day FROM training_days_mount WHERE user_statistics_id = :id AND training_day = :day";
                entityManager.createNativeQuery(querySearch)
                        .setParameter("id", user.getId())
                        .setParameter("day", date)
                        .getSingleResult();
            } catch (NoResultException e) {
                UserStatistics st = user.getStatistics();
                st = entityManager.find(UserStatistics.class, st.getId());
                st.getTrainingDaysInMonth().add(LocalDate.now());
                entityManager.flush();
                // запис не існує
            }
        }
    }

}
