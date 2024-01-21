package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "words_in_word_lesson")
@Data
public class WordInWordLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_lesson_id")
    private WordLesson wordLesson;

    @ManyToOne
    @JoinColumn(name = "vocabulary_page_id")
    private VocabularyPage vocabularyPage;

    @Column(name = "list_order")
    private Integer listOrder;
}
