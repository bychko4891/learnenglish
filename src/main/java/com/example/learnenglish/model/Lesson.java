package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;

@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy
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

}
