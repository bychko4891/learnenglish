package com.example.learnenglish.model;

import jakarta.persistence.*;

@Entity
@Table(name = "word_subcategory")
public class WordSubcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;
    @ManyToOne
    @JoinColumn(name = "word_category_id")
    private WordCategory wordCategory;
}
