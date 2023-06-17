package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTextOfAppPage;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.TextOfAppPageRepository;
import com.example.learnenglish.responsestatus.Message;
import com.example.learnenglish.responsestatus.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextOfAppPageService {
    private final TextOfAppPageRepository textOfAppPageRepository;


    public TextOfAppPageService(TextOfAppPageRepository textOfAppPageRepository) {
        this.textOfAppPageRepository = textOfAppPageRepository;
    }

    public ResponseMessage textOfAppPageEdit(DtoTextOfAppPage dtoTextOfAppPage) {
        Optional<TextOfAppPage> textOfAppPageOptional = textOfAppPageRepository.findById(dtoTextOfAppPage.getTextOfAppPage().getId());
        if (textOfAppPageOptional.isPresent()) {
            TextOfAppPage textOfAppPage = dtoTextOfAppPage.getTextOfAppPage();
            PageApplication pageApplication = dtoTextOfAppPage.getSelectedPageApplication();
            if (pageApplication.getId() == 0) {
                textOfAppPageRepository.save(textOfAppPage);
                return new ResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            } else {
                if (dtoTextOfAppPage.getTextOfAppPage().getId() == textOfAppPage.getId()) {
                    textOfAppPage.setPageApplication(pageApplication);
                    textOfAppPageRepository.save(textOfAppPage);
                    return new ResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
                } else {
                    return new ResponseMessage(Message.ERROR_SAVE_TEXT_OF_PAGE);
                }

            }
        } else {
            TextOfAppPage textOfAppPage = dtoTextOfAppPage.getTextOfAppPage();
            PageApplication pageApplication = dtoTextOfAppPage.getSelectedPageApplication();
            if (pageApplication.getId() != 0) {
                Optional<TextOfAppPage> textOfAppPageOptionalSearch = textOfAppPageRepository.searchTextOfAppPageByPageApplicationId(pageApplication.getId());
                if (textOfAppPageOptionalSearch.isEmpty()) {
                    textOfAppPage.setPageApplication(pageApplication);
                    textOfAppPageRepository.save(textOfAppPage);
                    return new ResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
                } else {
                    return new ResponseMessage(Message.ERROR_SAVE_TEXT_OF_PAGE);
                }
            } else {
                textOfAppPage.setPageApplication(null);
                textOfAppPageRepository.save(textOfAppPage);
                return new ResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            }

        }
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
        if (textOfAppPageOptional.isPresent()) {
            return textOfAppPageOptional.get();
        } else throw new IllegalArgumentException("TextOfAppPage with id " + id + " not found");

    }

}
