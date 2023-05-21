package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "learn_english_info")
@Getter
@Setter
public class LearnEnglishInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name="application_info", columnDefinition = "text")
    private String applicationInfo;

    @Column(name="registration_info", columnDefinition = "text")
    private String registrationInfo;

    @Column(name = "login_info", columnDefinition = "text")
    private String loginInfo;

    @Column(name = "text_download_info", columnDefinition = "text")
    private String textDownloadInfo;

    public LearnEnglishInfo() {
    }
}
