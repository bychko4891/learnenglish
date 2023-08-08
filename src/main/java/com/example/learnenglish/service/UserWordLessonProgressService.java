package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserWordLessonProgress;
import com.example.learnenglish.repository.UserWordLessonProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserWordLessonProgressService {

  private final UserWordLessonProgressRepository userWordLessonProgressRepository;
  private final WordLessonService wordLessonService;

  public void startWordLesson(User user, Long wordLessonId, boolean start){
    Optional<UserWordLessonProgress> userWordLessonProgressOptional = userWordLessonProgressRepository.findUserWordLessonProgressesByUserIdAndWordLessonId(user.getId(), wordLessonId);
    if(userWordLessonProgressOptional.isPresent()){
      UserWordLessonProgress userWordLessonProgress = userWordLessonProgressOptional.get();
      userWordLessonProgress.setStartLesson(start);
      userWordLessonProgressRepository.save(userWordLessonProgress);

    } else {
      UserWordLessonProgress userWordLessonProgress = new UserWordLessonProgress();
      userWordLessonProgress.setStartLesson(true);
      userWordLessonProgress.setUser(user);
      userWordLessonProgress.setWordLesson(wordLessonService.getWordLesson(wordLessonId));
      userWordLessonProgressRepository.save(userWordLessonProgress);
    }
  }

}
