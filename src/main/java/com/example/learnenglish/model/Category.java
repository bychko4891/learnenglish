package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column(name = "description", columnDefinition = "text") //Змінив поле!!!
    private String description; //Змінив поле!!!

    @Column
    private boolean mainCategory = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(name="view_subcategory_full_no_info_or_name_and_info")
    private boolean viewSubcategoryFullNoInfoOrNameAndInfo = false;



    @ElementCollection(targetClass = CategoryPage.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "category_page", joinColumns = @JoinColumn(name = "category_id"))
    @Enumerated(EnumType.STRING)
    private Set<CategoryPage> categoryPages = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;


    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subcategories = new ArrayList<>();

//    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "category")
//    private List<Word> words;

//    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "translationPairsPageCategory")
//    private List<TranslationPairsPage> translationPairsPages;

//    @OneToMany(mappedBy = "category")
//    private List<WordLesson> wordLessons;

    public Category() {
    }
}
