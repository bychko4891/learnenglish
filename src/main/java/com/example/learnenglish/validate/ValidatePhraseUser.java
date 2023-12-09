package com.example.learnenglish.validate;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.PhraseUserDto;
import java.util.regex.Pattern;

public class ValidatePhraseUser {


    public static boolean validateTranslationPairs(PhraseUserDto phraseUserDto) {

        if (Pattern.matches
                ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{4,20}\\b$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", phraseUserDto.getUkrTranslate()) &&

                Pattern.matches
                        ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", phraseUserDto.getEngPhrase())) {
            return true;
        }
        return false;
    }



}
