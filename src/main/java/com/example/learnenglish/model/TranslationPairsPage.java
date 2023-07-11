package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
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
    private boolean  published = false;

    @Column(columnDefinition = "text")
    private String info;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "translationPairsPage")
//    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER, mappedBy = "translationPairsPage")
    private List<TranslationPair> translationPairs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category translationPairsPageCategory;
}
