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
@Table(name = "words_with_order")
@Data
public class WordWithOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phrase_application_id")
    private PhraseApplication phraseApplication;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "list_order")
    private Integer listOrder;
}
