package com.example.learnenglish.model.users;

import com.example.learnenglish.model.WordLesson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table
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
