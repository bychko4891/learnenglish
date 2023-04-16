package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;

@Entity
@Table(name="translation_pair")
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="id_user_create")
    private int idUserCreate;
    @Column(name = "ukr_text")
    private String ukrText;
    @Column(name = "eng_text")
    private String engText;
    @Column(name = "audio_path")
    private String audioPath;
    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn
    private User user;

    public TranslationPair() {
    }

    public TranslationPair(String ukrText, String engText) {
        this.idUserCreate ++;
        this.ukrText = ukrText;
        this.engText = engText;
        this.audioPath = "path/no";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdUserCreate() {
        return idUserCreate;
    }

    public void setIdUserCreate(int idUserCreate) {
        this.idUserCreate = idUserCreate;
    }

    public String getUkrText() {
        return ukrText;
    }

    public void setUkrText(String ukrText) {
        this.ukrText = ukrText;
    }

    public String getEngText() {
        return engText;
    }

    public void setEngText(String engText) {
        this.engText = engText;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TranslationPair{" +
                "id=" + id +
                ", idUserCreate=" + idUserCreate +
                ", ukrText='" + ukrText + '\'' +
                ", engText='" + engText + '\'' +
                ", audioPath='" + audioPath + '\'' +
                ", lesson=" + lesson +
                ", user=" + user +
                '}';
    }
}

