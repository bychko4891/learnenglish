package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import java.util.ArrayList;
import java.util.List;

public class DtoUserStatisticsToUi {
    private List<Integer> studyTimeInTwoWeeks = new ArrayList<>();
    private Integer repetitionsCount;
    private Integer repetitionsCountPrev;
    private Integer repetitionsCountNow;
    private Integer daysInARowCount;
    private Integer errorCount;

    public List<Integer> getStudyTimeInTwoWeeks() {
        return studyTimeInTwoWeeks;
    }

    public void setStudyTimeInTwoWeeks(List<Integer> studyTimeInTwoWeeks) {
        this.studyTimeInTwoWeeks = studyTimeInTwoWeeks;
    }

    public Integer getRepetitionsCount() {
        return repetitionsCount;
    }

    public void setRepetitionsCount(Integer repetitionsCount) {
        this.repetitionsCount = repetitionsCount;
    }

    public Integer getRepetitionsCountPrev() {
        return repetitionsCountPrev;
    }

    public void setRepetitionsCountPrev(Integer repetitionsCountPrev) {
        this.repetitionsCountPrev = repetitionsCountPrev;
    }

    public Integer getRepetitionsCountNow() {
        return repetitionsCountNow;
    }

    public void setRepetitionsCountNow(Integer repetitionsCountNow) {
        this.repetitionsCountNow = repetitionsCountNow;
    }

    public Integer getDaysInARowCount() {
        return daysInARowCount;
    }

    public void setDaysInARowCount(Integer daysInARowCount) {
        this.daysInARowCount = daysInARowCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }
}
