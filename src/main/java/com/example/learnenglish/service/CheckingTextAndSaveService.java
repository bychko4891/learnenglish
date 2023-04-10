package com.example.learnenglish.service;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.responsestatus.*;
import com.example.learnenglish.model.TranslationPair;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CheckingTextAndSaveService {
    private final TranslationPairService translationPairService;

    public CheckingTextAndSaveService(TranslationPairService translationPairService) {
        this.translationPairService = translationPairService;
    }

    public ResponseStatus checkingRequestText(String ukrText, String engText) {
        ukrText = ukrText.trim();
        engText = engText.trim();
        ukrText = ukrText.replaceAll("\\s{2,}", "");
        engText = engText.replaceAll("\\s{2,}", "");
        ukrText = ukrText.replaceAll("\\b\\s\\B", "");
        engText = engText.replaceAll("\\b\\s\\B", "");
        if (Pattern.matches("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", ukrText) &&
                Pattern.matches("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                        "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", engText)) {
            try {
                translationPairService.saveTranslationPair(new Lesson(), new TranslationPair(ukrText, engText));
//                writeToFile.writeToFile();
                return new ResponseStatus(Message.SUCCESSADDBASE);
            } catch (NullPointerException e) {
                return new ResponseStatus(Message.ERRORBASE);
            }
//            return new TranslationPair(ukrText, engText);
        }
        return new ResponseStatus(Message.ERRORADDBASE);
    }
}
