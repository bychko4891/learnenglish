package com.example.learnenglish.model.users;

import com.example.learnenglish.model.Word;
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

    @OneToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean answerCorrect = false;
}
