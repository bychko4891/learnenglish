package com.example.learnenglish.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoTranslationPairToUI {
    private Long id;
    private String ukrText;
    private String engText;
    private String fragment;
    private List<String> engTextList;

    public DtoTranslationPairToUI() {
    }
    public DtoTranslationPairToUI(Long id, String ukrText, String engText) {
    }


    public void setEngTextList(List<String> engTextList) {
        this.engTextList = engTextList;
    }

    public List<String> getEngTextList() {
        return engTextList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
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
}
