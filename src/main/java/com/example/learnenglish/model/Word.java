package com.example.learnenglish.model;

import com.example.learnenglish.model.users.Image;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "words")
@Data
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String translate;

    @Column
    private String description;

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

    @Column(name = "info", columnDefinition = "text")
    private String info;

    @Column
    private boolean published = false;

//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinColumn(name = "word_lesson_id")
//    private WordLesson wordLesson;//
//
    @OneToOne(mappedBy = "word", fetch = FetchType.LAZY)
    private WordInWordLesson wordInWordLesson;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image images;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "word_audio_id")
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "word")
    private List<PhraseApplication> phraseExamples = new ArrayList<>();

}
