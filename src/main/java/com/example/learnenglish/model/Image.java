package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.users.User;
import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "images")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "image_name", length = 1000)
    @JsonView(JsonViews.ViewFieldName.class)
    private String imageName;

    @Column(length = 1000)
    private boolean webImage = false;


    @OneToOne(mappedBy = "userAvatar", cascade = CascadeType.ALL)
    private User user;

    public Image() {
    }
}
