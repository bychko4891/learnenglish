package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "words_audio")
public class WordAudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String brAudioName;

    @Column
    private String usaAudioName;

    @OneToOne(mappedBy = "wordAudio")
    private Word word;

    public WordAudio() {
    }

}
