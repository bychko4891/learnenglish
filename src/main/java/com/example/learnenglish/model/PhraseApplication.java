package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

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

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "eng_phrases",
//            joinColumns = @JoinColumn(name = "phrase_application_id"),
//            inverseJoinColumns = @JoinColumn(name = "word_id"))
    @OneToMany(mappedBy = "phraseApplication", cascade = {CascadeType.ALL, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("listOrder")
    private List<WordWithOrder> engPhrase = new ArrayList<>();

    @Column
    private String ukrTranslate;

    @Column
    private boolean questionForm = false;

    @Transient
    private boolean isRepeatable;

    @OneToOne
    private Audio audio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private PhraseLesson phraseLesson;

//    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
//    @JoinColumn(name = "category_id")
//    private Category category;
    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;
}
