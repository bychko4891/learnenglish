package com.example.learnenglish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class DtoTranslationPair {
    @Length(max = 150, message = " Length ukr")
    private String ukrText;
    @Length(max = 150, message = " Length ukrW")
    private String ukrTextWoman;

    @NotBlank(message = "Is blank")
    @Length(max = 150, message = " Length eng")
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

    public void setUkrText(String ukrTextMan) {
        this.ukrText = ukrTextMan;
    }

    public String getUkrTextWoman() {
        return ukrTextWoman;
    }

    public void setUkrTextWoman(String ukrTextWoman) {
        this.ukrTextWoman = ukrTextWoman;
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

}
