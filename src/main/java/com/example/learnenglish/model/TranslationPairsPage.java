package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "translation_pairs_pages")
public class TranslationPairsPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private boolean published = false;

    @Column(columnDefinition = "text")
    private String info;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phrase_and_phrases_page",
            joinColumns = @JoinColumn(name = "phrases_page_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id"))
    private List<TranslationPair> translationPairs = new ArrayList<>();

//    @OneToMany
//    @JoinColumn(name = "translation_pair_id")
//    private List<TranslationPair> translationPairs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category translationPairsPageCategory;
}