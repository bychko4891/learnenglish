package com.example.learnenglish.model;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.UserWordLessonProgress;
import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "word_lessons")
public class WordLesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonView(JsonViews.ViewFieldId.class)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int serialNumber;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "wordLesson", cascade = {CascadeType.ALL, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("listOrder")
    private List<WordInWordLesson> words = new ArrayList<>();

    @Transient
    private UserWordLessonProgress userWordLessonProgress;


    public WordLesson() {
    }
}
