package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "lesson_1")
public class Lesson {
    @Id
    private Long id;

    @Column(name = "lesson_id")
    private int lessonId;
    @ManyToOne
    private User user;


    public Lesson() {

    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
