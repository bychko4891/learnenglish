package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;

@Entity
@Table(name="translation_pair")
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name="lesson_counter")
    private int lessonCounter;
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

    public int getLessonCounter() {
        return lessonCounter;
    }

    public void setLessonCounter(int lessonCounter) {
        this.lessonCounter = lessonCounter;
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
}

