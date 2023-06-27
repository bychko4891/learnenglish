package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "words_category")
@Setter
@Getter
public class WordCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private boolean mainCategory = false;

    @Column(name = "info", columnDefinition = "text")
    private String info;

    @OneToMany
    @JoinColumn(name = "subcategory_id")
    private List<WordCategory> subcategory;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "wordCategory")
    private List<Word> words;

    @OneToOne
    private WordCategory parentCategory;

    public WordCategory() {
    }
}
