package com.example.learnenglish.dto;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CreateTranslationPair {
    private String ukr_text;
    private String eng_text;

    public ResponseStatus checkingRequestText(CreateTranslationPair createTranslationPair, Long lessonId, Long userID) {
        String ukrText = createTranslationPair.ukr_text;
        String engText = createTranslationPair.eng_text;
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
//                translationPairService.saveTranslationPair(new Lesson(), new TranslationPair(ukrText, engText));
//                writeToFile.writeToFile();
                return new ResponseStatus(Message.SUCCESSADDBASE);
            } catch (NullPointerException e) {
                return new ResponseStatus(Message.ERRORBASE);
            }
//            return new TranslationPair(ukrText, engText);
        }
        return new ResponseStatus(Message.ERRORADDBASE);

//    public String getUkr_text() {
//        return ukr_text;
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

    @Override
    public String toString() {
        return "CreateTranslationPair{" +
                "ukr_text='" + ukr_text + '\'' +
                ", eng_text='" + eng_text + '\'' +
                '}';
    }
}
