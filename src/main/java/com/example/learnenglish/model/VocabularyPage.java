package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.Image;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vocabulary_page")
@Data
public class VocabularyPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "text")
    private String cardInfo;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private boolean published = false;
    @OneToOne
    @JoinColumn(name = "word_id", referencedColumnName = "id")
    private Word word;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phrase_examples",
            joinColumns = @JoinColumn(name = "vocabulary_page_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_application_id"))
    private List<PhraseApplication> phraseExamples = new ArrayList<>();


}
