package com.example.learnenglish.model;

import com.example.learnenglish.model.users.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "words")
@Setter
@Getter
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image images;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "word_lesson_id")
    private WordLesson wordLesson;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "word_audio_id")
    private Audio audio;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phrase_and_word",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id"))
    private List<PhraseUser> phraseUsers;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category wordCategory;

    public Word() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word)) return false;
        return isRepeatable == word.isRepeatable && published == word.published && Objects.equals(id, word.id) && Objects.equals(name, word.name) && Objects.equals(translate, word.translate) && Objects.equals(description, word.description) && Objects.equals(brTranscription, word.brTranscription) && Objects.equals(usaTranscription, word.usaTranscription) && Objects.equals(irregularVerbPt, word.irregularVerbPt) && Objects.equals(irregularVerbPp, word.irregularVerbPp) && Objects.equals(info, word.info) && Objects.equals(images, word.images) && Objects.equals(wordLesson, word.wordLesson) && Objects.equals(audio, word.audio) && Objects.equals(phraseUsers, word.phraseUsers) && Objects.equals(wordCategory, word.wordCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, translate, description, brTranscription, usaTranscription, irregularVerbPt, irregularVerbPp, isRepeatable, info, published, images, wordLesson, audio, phraseUsers, wordCategory);
    }
}
