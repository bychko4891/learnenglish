package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "phrases_application")
public class PhraseApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToMany
    private List<Word> engPhrase = new ArrayList<>();

    @Column
    private String ukrTranslate;

    @Transient
    private boolean isRepeatable;

    @OneToOne
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
