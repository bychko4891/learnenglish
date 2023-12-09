package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Setter
@Getter
public class DtoTranslationPair {

    private Long id;

    @NotBlank(message = "Поле українського тексту не повинно бути пустим.")
    @Length(max = 150, message = "Вибачте, але в полі з українським текстом дозволено довжину речення максимум 150 символів!")
    @Length(min = 4, message = "Ваше речення дуже коротке")
    private String ukrText;

    @Length(max = 150, message = "Вибачте, але дозволено довжину речення максимум 150 символів разом з пропусками!")
    @Length(min = 4, message = "Ваше речення дуже коротке")
    private String ukrTextFemale;

    @NotBlank(message = "Поле англійського тексту не повинно бути пустим.")
    @Length(max = 150, message = "Вибачте, але в полі з англійським текстом дозволено довжину речення максимум 150 символів!")
    @Length(min = 4, message = "Ваше речення дуже коротке")
    private String engText;

//    private Long lessonId;

//    private Long userId;

    public DtoTranslationPair() {
    }

}
