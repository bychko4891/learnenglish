package com.example.learnenglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "words")
@Data
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonView(JsonViews.ViewFieldId.class)
    private Long id;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String name;

    @Column
    private String translate;

//    @Column
//    private String description;

    @Column
    private String brTranscription;

    @Column
    private String usaTranscription;

    @Column
    private String irregularVerbPt;

    @Column
    private String irregularVerbPp;

    @Column
    private boolean activeURL = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_id")
    private Audio audio;


}
