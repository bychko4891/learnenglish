package com.example.learnenglish.dto;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoTranslationPairToUI {
    private String ukrText;
    private String engText;
    private String fragment;

    public List<String> getEngTextList() {
        return engTextList;
    }

    public void setEngTextList(List<String> engTextList) {
        this.engTextList = engTextList;
    }

    private List<String> engTextList;

    public DtoTranslationPairToUI() {
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

    @Override
    public String toString() {
        return "DtoTranslationPairToUI{" +
                "ukrText='" + ukrText + '\'' +
                ", engText='" + engText + '\'' +
                '}';
    }
}
