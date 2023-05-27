package com.example.learnenglish.service;

import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.TextOfAppPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextOfAppPageService {
    private final TextOfAppPageRepository textOfAppPageRepository;

    public TextOfAppPageService(TextOfAppPageRepository textOfAppPageRepository) {
        this.textOfAppPageRepository = textOfAppPageRepository;
    }
public void saveTextOfApppage(TextOfAppPage textOfAppPage){
        textOfAppPageRepository.save(textOfAppPage);
}
    public List<TextOfAppPage> getAppTextPageList(){
//        List<AppInfoText> appInfoTextList = appInfoRepository.findById(1l).get().getAppInfoTextPage();
        return (List<TextOfAppPage>) textOfAppPageRepository.findAll();
    }
    public Long countTextOfAppPage(){
        return textOfAppPageRepository.count();
    }
}
