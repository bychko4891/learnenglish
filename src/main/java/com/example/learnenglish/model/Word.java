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

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "word_catalog_id")
    private WordCategory wordCategory;

    public Word() {
    }
}
