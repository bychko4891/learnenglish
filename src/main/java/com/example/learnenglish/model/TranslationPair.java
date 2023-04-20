package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name="translation_pair")//, uniqueConstraints = {
//        @UniqueConstr//aint(columnNames = {"ukr_text", "eng_text"})
//        @UniqueConstraint(columnNames = {"id", "lesson_id"})
//})
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name="lesson_counter")
    private Long lessonCounter;
    @Column(name = "ukr_text")
//    @ColumnTransformer(read = "ukr_text::citext", write = "?::citext")
    private String ukrText;
    @Column(name = "eng_text")
//    @ColumnTransformer(read = "eng_text::citext", write = "?::citext")
    private String engText;
    @Column(name = "audio_path")
    private String audioPath;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn
    private User user;

    public TranslationPair() {
    }

    public Long getLessonCounter() {
        return lessonCounter;
    }

    public void setLessonCounter(Long lessonCounter) {
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

    @Override
    public String toString() {
        return "TranslationPair{" +
                "id=" + id +
                ", lessonCounter=" + lessonCounter +
                ", ukrText='" + ukrText + '\'' +
                ", engText='" + engText + '\'' +
                ", audioPath='" + audioPath + '\'' +
                ", lessonId=" + lesson.getId() +
                ", userId=" + user.getId() +
                '}';
    }
}

