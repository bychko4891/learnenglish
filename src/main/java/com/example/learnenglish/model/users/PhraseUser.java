package com.example.learnenglish.model.users;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PhraseAndUser;
import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name="phrases_user")
public class PhraseUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @Column(name="lesson_counter")
//    private Long lessonCounter;

    @Column(name = "ukr_translate", length = 500)
    private String ukrTranslate;

//    @Column(name = "ukr_text_female", length = 500)
//    private String ukrTextFemale;

    @Column(name = "eng_phrase", length = 500)
    private String engPhrase;

    @Transient
    private boolean isRepeatable;

    @OneToOne(mappedBy = "phraseUser", cascade = CascadeType.ALL)
    private PhraseAndUser phraseAndUser;

//    @Column
//    private boolean userTranslationPair = false;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "audio_id")
//    private Audio audio;

//    @ManyToOne
//    @JoinColumn(name = "lesson_id")
//    private Lesson lesson;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    public PhraseUser() {
    }

}

