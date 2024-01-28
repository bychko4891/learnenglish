package com.example.learnenglish.model.users;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String uuid;

    @Column(name = "login")
    private String login;

    @Column(name="name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "activation_code", length = 1000)
    private String activationCode;

    @Column(name = "active")
    private boolean active;

    @Column(name = "user_phrases_in_lesson")
    private boolean userPhrasesInLesson;

    @Column(name = "user_ip")
    private String userIp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private Image userAvatar;

    @Column(name = "password", length = 1000)
    private String password;

    @Column(length = 300)
    private String about;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private UserStatistics statistics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserWordLessonProgress> wordLessonProgress = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<PhraseUser> phraseUsers = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> authority = new HashSet<>();

    @ElementCollection(targetClass = UserGender.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "gender", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserGender> gender = new HashSet<>();

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @Column(name = "last_visit")
    private LocalDateTime lastVisit;
    @PrePersist
    private void init(){
        this.dateOfCreated = LocalDateTime.now();
        this.uuid = UUID.randomUUID().toString();
    }

    public User() {
    }

}
