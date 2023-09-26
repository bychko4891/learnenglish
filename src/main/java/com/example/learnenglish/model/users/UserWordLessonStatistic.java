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

@Entity
@Getter
@Setter
@Table(name="user_word_lesson_statistics")
public class UserWordLessonStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_lesson_id")
    private WordLesson wordLesson;

    @Column
    private String word;

    @Column
    private String wordInfo;

    @Column
    private String userAnswer;

    @Column
    private Long wordLessonCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean answerCorrect = false;
}
