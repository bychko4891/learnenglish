package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;

@Entity
@Table(name = "texts_of_app_pages")
public class TextOfAppPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "text", columnDefinition = "text")
    private String text;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "page_application_id")
    private PageApplication pageApplication;

    public TextOfAppPage() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PageApplication getPageApplication() {
        return pageApplication;
    }

    public void setPageApplication(PageApplication pageApplication) {
        this.pageApplication = pageApplication;
    }
}
