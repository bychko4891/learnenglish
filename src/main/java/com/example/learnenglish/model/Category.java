package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonView(JsonViews.ViewFieldId.class)
    private Long id;

    @Column
    private String uuid;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String name;

    @Column(name = "description", columnDefinition = "text") //Змінив поле!!!
    private String description; //Змінив поле!!!

    @Column
    @JsonView(JsonViews.ViewFieldOther.class)
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
    @JsonView(JsonViews.ViewFieldOther.class)
    private Category parentCategory;


    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    @JsonView(JsonViews.ViewFieldOther.class)
    private List<Category> subcategories = new ArrayList<>();


    @Transient
    private int countWordLessons;

    @PrePersist
    private void init(){
        this.uuid = UUID.randomUUID().toString();
    }


    public Category() {
    }
}
