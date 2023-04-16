package com.example.learnenglish.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class DtoTranslationPair {
    @NotNull
    private String ukr_text;
    @NotNull
    private String eng_text;
    @NotNull
    private Integer lessonId;
    @NotNull
    private long userId;

    public DtoTranslationPair() {
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUkr_text() {
        return ukr_text;
    }

    public void setUkr_text(String ukr_text) {
        this.ukr_text = ukr_text;
    }

    public String getEng_text() {
        return eng_text;
    }

    public void setEng_text(String eng_text) {
        this.eng_text = eng_text;
    }

    public DtoTranslationPair(String ukr_text, String eng_text, Integer lessonId, long userId) {
        this.ukr_text = ukr_text;
        this.eng_text = eng_text;
        this.lessonId = lessonId;
        this.userId = userId;
    }
}
