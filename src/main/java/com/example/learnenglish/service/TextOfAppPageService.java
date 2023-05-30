package com.example.learnenglish.service;

import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.TextOfAppPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextOfAppPageService {
    private final TextOfAppPageRepository textOfAppPageRepository;

    public TextOfAppPageService(TextOfAppPageRepository textOfAppPageRepository) {
        this.textOfAppPageRepository = textOfAppPageRepository;
    }

    public void textOfAppPageEdit(TextOfAppPage textOfAppPage) {
        Optional<TextOfAppPage> textOfAppPageOptional = textOfAppPageRepository.findById(textOfAppPage.getId());
        if (!textOfAppPageOptional.isPresent()) {
            textOfAppPageSave(textOfAppPage);
        } else {
            TextOfAppPage textOfAppPageBase = textOfAppPageOptional.get();
            textOfAppPageBase.setName(textOfAppPage.getName());
            textOfAppPageBase.setText(textOfAppPage.getText());
            textOfAppPageRepository.save(textOfAppPageBase);
        }
    }

    public void textOfAppPageSave(TextOfAppPage textOfAppPage) {
        textOfAppPageRepository.save(textOfAppPage);
    }

    public List<TextOfAppPage> getAppTextPageList() {
//        List<AppInfoText> appInfoTextList = appInfoRepository.findById(1l).get().getAppInfoTextPage();
        return (List<TextOfAppPage>) textOfAppPageRepository.findAll();
    }

    public Long countTextOfAppPage() {
        return textOfAppPageRepository.count();
    }

    public TextOfAppPage findByIdTextOfAppPage(Long id) {
        Optional<TextOfAppPage> textOfAppPageOptional = textOfAppPageRepository.findById(id);
        if(textOfAppPageOptional.isPresent()){
            return textOfAppPageOptional.get();
        } else throw new IllegalArgumentException("TextOfAppPage with id " + id + " not found");

    }

}
