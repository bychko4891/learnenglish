package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @Transient
    private boolean isRepeatable = true;

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column
    private boolean published = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "word_audio_id")
    private Audio audio;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phrase_and_word",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id"))
    private List<TranslationPair> translationPairs;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category wordCategory;

    public Word() {
    }
}
