package com.example.learnenglish.model.users;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.WordLesson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "user_word_lesson_progress")
public class UserWordLessonProgress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "word_id")
    private WordLesson wordLesson;

    @Column
    private int rating;

    @Column
    private boolean startLesson = false;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
