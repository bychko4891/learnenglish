package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PhraseUserDto {
//    private Long id;

    @NotBlank(message = "Поле англійського тексту не повинно бути пустим.")
    @Length(max = 150, message = "Вибачте, але в полі з англійським текстом дозволено довжину речення максимум 150 символів!")
    @Length(min = 4, message = "Ваше речення дуже коротке")
    private String engPhrase;


    @NotBlank(message = "Поле українського тексту не повинно бути пустим.")
    @Length(max = 150, message = "Вибачте, але в полі з українським текстом дозволено довжину речення максимум 150 символів!")
    @Length(min = 4, message = "Ваше речення дуже коротке")
    private String ukrTranslate;

}
