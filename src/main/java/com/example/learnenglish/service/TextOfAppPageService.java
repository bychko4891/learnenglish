package com.example.learnenglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTextOfAppPage;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.TextOfAppPageRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextOfAppPageService {
    private final TextOfAppPageRepository textOfAppPageRepository;
    private final PageApplicationService pageApplicationService;

    public CustomResponseMessage textOfAppPageEdit(DtoTextOfAppPage dtoTextOfAppPage) {
        Optional<TextOfAppPage> textOfAppPageOptional = textOfAppPageRepository.findById(dtoTextOfAppPage.getTextOfAppPage().getId());
        TextOfAppPage textOfAppPage = dtoTextOfAppPage.getTextOfAppPage();
        if (textOfAppPageOptional.isPresent()) {
            TextOfAppPage textOfAppPageBase = textOfAppPageOptional.get();
            textOfAppPageBase.setName(textOfAppPage.getName());
            textOfAppPageBase.setText(textOfAppPage.getText());
            if (dtoTextOfAppPage.getSelectedPageApplication().getId() == 0) {
                if(textOfAppPageBase.getPageApplication() != null && !dtoTextOfAppPage.getSelectedPageApplication().getAddress().isBlank()){
                    textOfAppPageBase.getPageApplication().setAddress(dtoTextOfAppPage.getSelectedPageApplication().getAddress());
                }
                textOfAppPageRepository.save(textOfAppPageBase);
                return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            } else {
                PageApplication pageApplication = pageApplicationService.getPageApplication(dtoTextOfAppPage.getSelectedPageApplication().getId());
                if(searchTextOfAppPageByPageApplicationId(pageApplication.getId()).isEmpty() ||
                        textOfAppPageBase.getPageApplication().getId().equals(pageApplication.getId())) {
                    if(!dtoTextOfAppPage.getSelectedPageApplication().getAddress().isBlank()){
                        pageApplication.setAddress(dtoTextOfAppPage.getSelectedPageApplication().getAddress());
                    }
                    textOfAppPageBase.setPageApplication(pageApplication);
                    textOfAppPageRepository.save(textOfAppPageBase);
                    return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
                }else{
                    return new CustomResponseMessage(Message.ERROR_SAVE_TEXT_OF_PAGE);
                }
            }
        } else return saveNewTextOfAppPage(dtoTextOfAppPage);
    }

    private CustomResponseMessage saveNewTextOfAppPage(DtoTextOfAppPage dtoTextOfAppPage){
        TextOfAppPage textOfAppPage = new TextOfAppPage();
        textOfAppPage.setName(dtoTextOfAppPage.getTextOfAppPage().getName());
        textOfAppPage.setText(dtoTextOfAppPage.getTextOfAppPage().getText());
        if (dtoTextOfAppPage.getSelectedPageApplication().getId() != 0) {
            if (searchTextOfAppPageByPageApplicationId(dtoTextOfAppPage.getSelectedPageApplication().getId()).isEmpty()) {
                PageApplication pageApplication = pageApplicationService.getPageApplication(dtoTextOfAppPage.getSelectedPageApplication().getId());
                pageApplication.setAddress(dtoTextOfAppPage.getSelectedPageApplication().getAddress());
                textOfAppPage.setPageApplication(pageApplication);
                textOfAppPageRepository.save(textOfAppPage);
                return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            } else {
                return new CustomResponseMessage(Message.ERROR_SAVE_TEXT_OF_PAGE);
            }
        } else {
            textOfAppPageRepository.save(textOfAppPage);
            return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
        }
    }

    private Optional<TextOfAppPage> searchTextOfAppPageByPageApplicationId(long  pageApplicationId){
        return textOfAppPageRepository.searchTextOfAppPageByPageApplicationId(pageApplicationId);
    }

    public List<TextOfAppPage> getAppTextPageList() {
        return (List<TextOfAppPage>) textOfAppPageRepository.findAll();
    }

    public Long countTextOfAppPage() {
        return textOfAppPageRepository.count();
    }

    public TextOfAppPage findByIdTextOfAppPage(long id) {
        Optional<TextOfAppPage> textOfAppPageOptional = textOfAppPageRepository.findById(id);
        if (textOfAppPageOptional.isPresent()) {
            return textOfAppPageOptional.get();
        } else throw new IllegalArgumentException("TextOfAppPage with id " + id + " not found");

    }

}
