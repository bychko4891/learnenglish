package com.example.learnenglish.repository;

import com.example.learnenglish.model.users.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
//    select count(training_day) from training_days_mount where user_statistics_id = '1';
//    @Query("select count (s.training_day) from  s where s.user_s")
}
