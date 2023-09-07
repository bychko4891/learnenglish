package com.example.learnenglish.dto;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DtoUserWordLessonStatisticToUi {

    private List<DtoUserWordLessonStatistic> dtoUserWordLessonStatisticErrorList;

    private double rating;

    public DtoUserWordLessonStatisticToUi() {
    }
}
