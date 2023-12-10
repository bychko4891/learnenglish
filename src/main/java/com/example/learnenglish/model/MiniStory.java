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
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "mini_story")
@Data
public class MiniStory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private boolean published = false;

    @Column(columnDefinition = "text")
    private String story;

    @OneToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "phrase_and_phrases_page",
//            joinColumns = @JoinColumn(name = "phrases_page_id"),
//            inverseJoinColumns = @JoinColumn(name = "phrase_id"))
//    private List<PhraseUser> phraseUsers = new ArrayList<>();

//    @OneToMany
//    @JoinColumn(name = "translation_pair_id")
//    private List<TranslationPair> translationPairs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}