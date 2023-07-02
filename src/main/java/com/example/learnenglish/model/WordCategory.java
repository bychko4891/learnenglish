package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "words_category")
@Setter
@Getter
public class WordCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private boolean mainCategory = false;

    @Column(name="view_subcategory_full_no_info_or_name_and_info")
    private boolean viewSubcategoryFullNoInfoOrNameAndInfo = false;

    @Column(name = "info", columnDefinition = "text")
    private String info;

//    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private WordCategory parentCategory;

//    @JsonBackReference
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<WordCategory> subcategories = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "wordCategory")
    private List<Word> words;


    public WordCategory() {
    }
}
