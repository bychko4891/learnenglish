package com.example.learnenglish.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "lesson_info", columnDefinition = "text")
    private String lessonInfo;

    public Lesson() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLessonInfo() {
        return lessonInfo;
    }

    public void setLessonInfo(String lessonInfo) {
        this.lessonInfo = lessonInfo;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lessonInfo='" + lessonInfo + '\'' +
                '}';
    }
}
