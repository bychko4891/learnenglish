package com.example.learnenglish.dto;

import com.example.learnenglish.model.Word;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DtoWordToUI {
    private Long id;
    private String name;
    private String translate;
    private String description;
    private String imageName;
    private String transcription;
    private String audioName;
    private Long wordLessonId;
    private int totalPage;

    public DtoWordToUI() {
    }

    public static DtoWordToUI convertToDTO(Word word){
        DtoWordToUI dtoWordToUI = new DtoWordToUI();
        dtoWordToUI.setId(word.getId());
        dtoWordToUI.setName(word.getName());
        dtoWordToUI.setTranscription(word.getUsaTranscription());
        dtoWordToUI.setTranslate(word.getTranslate());
        dtoWordToUI.setDescription(word.getDescription());
        dtoWordToUI.setWordLessonId(word.getWordLesson().getId());
        dtoWordToUI.setImageName(word.getImages().getImageName());
        dtoWordToUI.setAudioName(word.getAudio().getUsaAudioName());
        return dtoWordToUI;
    }
}
