package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="translation_pair")
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name="lesson_counter")
    private Long lessonCounter;
    @Column(name = "ukr_text", length = 1000)
//    @ColumnTransformer(read = "ukr_text::citext", write = "?::citext")
    private String ukrText;
    @Column(name = "eng_text", length = 1000)
//    @ColumnTransformer(read = "eng_text::citext", write = "?::citext")
    private String engText;
    @Column(name = "audio_path")
    private String audioPath;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    public TranslationPair() {
    }

}

