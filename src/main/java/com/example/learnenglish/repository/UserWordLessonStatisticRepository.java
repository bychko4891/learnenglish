package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.UserWordLessonStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWordLessonStatisticRepository extends JpaRepository<UserWordLessonStatistic, Long> {

    void removeAllByUserIdAndWordLessonId(Long userId, Long wordLessonId);

    List<UserWordLessonStatistic> findAllByUserIdAndWordLessonId(Long userId, Long wordLessonId);
}
