package com.example.learnenglish.dto;

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.users.UserWordLessonStatistic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoUserWordLessonStatistic {
    private Long wordLessonId;

    private User user;

    private Long wordId;

    private String word;

    private String userAnswer;

    private String wordInfo;
    private boolean answerCorrect = false;

    public DtoUserWordLessonStatistic() {
    }

    public static DtoUserWordLessonStatistic convertToDto(UserWordLessonStatistic wordLessonStatistic){
        DtoUserWordLessonStatistic dtoUserWordLessonStatistic = new DtoUserWordLessonStatistic();

        return dtoUserWordLessonStatistic;
    }
}
