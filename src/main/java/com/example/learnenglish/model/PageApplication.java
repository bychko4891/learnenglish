package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
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
}