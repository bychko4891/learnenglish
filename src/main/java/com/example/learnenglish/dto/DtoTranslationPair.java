package com.example.learnenglish.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class DtoTranslationPair {
    @NotNull
    private String ukrText;
    @NotNull
    private String engText;
    @NotNull
    private Long lessonId;
    @NotNull
    private Long userId;

    public DtoTranslationPair() {
    }

    public String getUkrText() {
        return ukrText;
    }

    public void setUkrText(String ukrText) {
        this.ukrText = ukrText;
    }

    public String getEngText() {
        return engText;
    }

    public void setEngText(String engText) {
        this.engText = engText;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DtoTranslationPair{" +
                "ukrText='" + ukrText + '\'' +
                ", engText='" + engText + '\'' +
                ", lessonId=" + lessonId +
                ", userId=" + userId +
                '}';
    }
}
