package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "words")
@Setter
@Getter
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String translate;
    @Column
    private String brTranscription;
    @Column
    private String usaTranscription;
    @Column
    private String irregularVerbPt;
    @Column
    private String irregularVerbPp;
    @Column
    private String brAudioName;
    @Column
    private String usaAudioName;

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column
    private boolean published = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "word_audio_id")
    private WordAudio wordAudio;

    @ManyToOne
    @JoinColumn(name = "word_catalog_id")
    private WordCategory wordCategory;

    public Word() {
    }
}
