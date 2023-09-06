package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoUserWordLessonStatistic;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.model.users.UserWordLessonStatistic;
import com.example.learnenglish.repository.UserWordLessonStatisticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWordLessonStatisticService {
    private final UserWordLessonStatisticRepository userWordLessonStatisticRepository;
    private final WordService wordService;
    private final WordLessonService wordLessonService;

    public List<UserWordLessonStatistic> getUserWordLessonStatisticList(Long userId, Long wordLessonId) {
        return userWordLessonStatisticRepository.findAllByUserIdAndWordLessonId(userId, wordLessonId);
    }

    public void saveUserWordLessonStatistic(DtoUserWordLessonStatistic dtoUserWordLessonStatistics) {
        UserWordLessonStatistic userWordLessonStatistic = new UserWordLessonStatistic();
        Word word = wordService.getWord(dtoUserWordLessonStatistics.getWordId());
        WordLesson wordLesson = wordLessonService.getWordLesson(dtoUserWordLessonStatistics.getWordLessonId());
        userWordLessonStatistic.setUser(dtoUserWordLessonStatistics.getUser());
        userWordLessonStatistic.setWordLesson(wordLesson);
        userWordLessonStatistic.setWord(word.getName());
        String wordUserAnswer = StringUtils.normalizeSpace(dtoUserWordLessonStatistics.getUserAnswer());
        userWordLessonStatistic.setUserAnswer(wordUserAnswer);
        if (wordUserAnswer.equalsIgnoreCase(word.getName())) userWordLessonStatistic.setAnswerCorrect(true);
        userWordLessonStatisticRepository.save(userWordLessonStatistic);
    }
    @Transactional
    public void deleteWordLessonStatistic(Long userId, Long wordLessonId){
        userWordLessonStatisticRepository.removeAllByUserIdAndWordLessonId(userId, wordLessonId);
    }

    public void resultWordLessonAudit(){}
}
