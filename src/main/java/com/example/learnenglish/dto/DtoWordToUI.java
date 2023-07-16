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

    public DtoWordToUI() {
    }

    public static DtoWordToUI convertToDTO(Word word){
        DtoWordToUI dtoWordToUI = new DtoWordToUI();
        dtoWordToUI.setId(word.getId());
        dtoWordToUI.setName(word.getName());
        dtoWordToUI.setTranslate(word.getTranslate());
        return dtoWordToUI;
    }
}
