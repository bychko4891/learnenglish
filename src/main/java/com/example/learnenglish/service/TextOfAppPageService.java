package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTextOfAppPage;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.TextOfAppPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextOfAppPageService {
    private final TextOfAppPageRepository textOfAppPageRepository;
    private final PageApplicationService pageApplicationService;

    public TextOfAppPageService(TextOfAppPageRepository textOfAppPageRepository, PageApplicationService pageApplicationService) {
        this.textOfAppPageRepository = textOfAppPageRepository;
        this.pageApplicationService = pageApplicationService;
    }

    public void textOfAppPageEdit(DtoTextOfAppPage dtoTextOfAppPage) {
        Optional<TextOfAppPage> textOfAppPageOptional = textOfAppPageRepository.findById(dtoTextOfAppPage.getTextOfAppPage().getId());
        if (!textOfAppPageOptional.isPresent()) {
            textOfAppPageSave(dtoTextOfAppPage.getTextOfAppPage()); // -------------------------------------
        } else {
            TextOfAppPage textOfAppPageBase = textOfAppPageOptional.get();
            textOfAppPageBase.setName(dtoTextOfAppPage.getTextOfAppPage().getName());
            textOfAppPageBase.setText(dtoTextOfAppPage.getTextOfAppPage().getText());
            PageApplication pageApplication = pageApplicationService.getPageApplication(dtoTextOfAppPage.getPageApplicationId());
            textOfAppPageBase.setPageApplication(pageApplication);
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
