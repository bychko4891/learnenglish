package com.example.learnenglish.model;

import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "audio")
public class Audio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String brAudioName;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String usaAudioName;

//    @OneToOne(mappedBy = "audio")
//    private Word word;

//    @OneToOne(mappedBy = "audio")
//    private TranslationPair translationPair;

    public Audio() {
    }

}
