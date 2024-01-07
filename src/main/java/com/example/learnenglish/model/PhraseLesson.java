package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "phrase_lessons") // змінив поле!!!!!!!!!!!!!!!!!!!!!!!
@Data
public class PhraseLesson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "published")
    private boolean published = false;

    @Column(name = "description", columnDefinition = "text")  // змінив поле!!!!!!!!!!!!!!!!!!!!!!!
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
