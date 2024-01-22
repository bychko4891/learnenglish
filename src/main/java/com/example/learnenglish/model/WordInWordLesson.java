package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "words_in_word_lesson")
@Data
public class WordInWordLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
//    @JsonView(JsonViews.ViewFieldId.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_lesson_id")
    @JsonView(JsonViews.ViewFieldOther.class)
    private WordLesson wordLesson;

    @ManyToOne
    @JoinColumn(name = "vocabulary_page_id", referencedColumnName = "id")
    @JsonView(JsonViews.ViewFieldWord.class)
    private VocabularyPage vocabularyPage;

    @Column(name = "list_order")
    private Integer listOrder;

    @Transient
    @JsonView(JsonViews.ViewFieldOther.class)
    private int totalPage;

}
