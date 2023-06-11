package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class DtoTranslationPair {

    @NotBlank(message = "Поле українського тексту не повинно бути пустим.")
    @Length(max = 350, message = "Вибачте, але в полі з українським текстом дозволено довжину речення максимум 300 символів!")
    private String ukrText;
    @Length(max = 350, message = "Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!")
    private String ukrTextWoman;

    @NotBlank(message = "Поле англійського тексту не повинно бути пустим.")
    @Length(max = 350, message = "Вибачте, але в полі з англійським текстом дозволено довжину речення максимум 300 символів!")
    private String engText;

    private Long lessonId;

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
