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

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="translation_pair")
public class TranslationPair implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="lesson_counter")
    private Long lessonCounter;

    @Column(name = "ukr_text", length = 500)
    private String ukrText;

    @Column(name = "ukr_text_female", length = 500)
    private String ukrTextFemale;

    @Column(name = "eng_text", length = 500)
    private String engText;

    @Transient
    private boolean isRepeatable;

    @Column
    private boolean userTranslationPair = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    public TranslationPair() {
    }

}

