package com.example.learnenglish.model.users;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "image_name", length = 1000)
    private String imageName;

    @Column(length = 1000)
    private boolean webImage = false;


    @OneToOne(mappedBy = "userAvatar", cascade = CascadeType.ALL)
    private User user;

    public Images() {
    }
}
