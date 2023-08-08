package com.example.learnenglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.UserWordLessonProgress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWordLessonProgressRepository extends CrudRepository<UserWordLessonProgress, Long> {

    Optional<UserWordLessonProgress> findUserWordLessonProgressesByUserIdAndWordLessonId(Long userId, Long wordLessonId);
}
