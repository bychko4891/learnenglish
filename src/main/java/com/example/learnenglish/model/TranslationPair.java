package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name="translation_pair")
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="lesson_counter")
    private Long lessonCounter;

    @Column(name = "ukr_text", length = 500)
    private String ukrText;

    @Column(name = "ukr_text_female", length = 500)
    private String ukrTextFemale;

    @Column(name = "eng_text", length = 500)
    private String engText;

    @Column(name = "is_repeatable")
    private boolean isRepeatable = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "translation_pair_page_id")
    private TranslationPairsPage translationPairsPage;

    public TranslationPair() {
    }

}

