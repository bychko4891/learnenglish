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

@Getter
@Setter
@ToString
@Entity
@Table(name="translation_pair")
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="lesson_counter")
    private Long lessonCounter;

    @Column(name = "ukr_text", length = 1000)
    private String ukrText;

    @Column(name = "ukr_text_woman", length = 1000)
    private String ukrTextWoman;

    @Column(name = "eng_text", length = 1000)
    private String engText;

    @Column(name = "audio_path")
    private String audioPath;

    @Column(name = "edit_mode")
    private boolean editMode;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    public TranslationPair() {
    }

}

