package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.PhraseUser;
import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "phrases_and_user") // Змінив поле
public class PhraseAndUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phrase_user_id") // Змінив поле
    private PhraseUser phraseUser;

    @ManyToOne
    @JoinColumn(name = "phrase_application_id") // змінив поле
    private PhraseApplication phraseApplication;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_repetable")
    private boolean isRepeatable = true;

    public PhraseAndUser() {
    }
}
