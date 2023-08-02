package com.example.learnenglish.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "pages_application")
public class PageApplication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "name_page")
    private String namePage;
    @Column
    private String address;
//    @OneToOne(mappedBy = "pageApplication", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @OneToOne(mappedBy = "pageApplication")
    private TextOfAppPage textOfAppPage;

    public PageApplication() {
    }
    public PageApplication(String namePage) {
        this.namePage = namePage;
    }

    public PageApplication(Long id, String namePage) {
        this.id = id;
        this.namePage = namePage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamePage() {
        return namePage;
    }

    public void setNamePage(String namePage) {
        this.namePage = namePage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public TextOfAppPage getTextOfAppPage() {
        return textOfAppPage;
    }

    public void setTextOfAppPage(TextOfAppPage textOfAppPage) {
        this.textOfAppPage = textOfAppPage;
    }
}