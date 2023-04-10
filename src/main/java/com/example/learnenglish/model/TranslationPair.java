package com.example.learnenglish.model;

import jakarta.persistence.*;

@Entity
@Table(name="translation_pair")
public class TranslationPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ukr_text")
    private String ukrText;
    @Column(name = "eng_text")
    private String engText;
    @Column(name = "audio_path")
    private String audioPath;

    public TranslationPair() {
    }

    public TranslationPair(String ukrText, String engText) {
        this.ukrText = ukrText;
        this.engText = engText;
        this.audioPath = "path/no";
    }


    public int getId() {
        return this.id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getUkrText() {
        return ukrText;
    }

//    public void setUkrText(String ukrText) {
//        this.ukrText = ukrText;
//    }

    public String getEngText() {
        return engText;
    }

//    public void setEngText(String engText) {
//        this.engText = engText;
//    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    @Override
    public String toString() {
        return "TranslationPair{" +
                "id=" + id +
                ", ukrText='" + ukrText + '\'' +
                ", engText='" + engText + '\'' +
                ", audioPath='" + audioPath + '\'' +
                '}';
    }
}

