package com.example.learnenglish.service;

import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.PageApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// не Буде змінюватись
@Service
public class PageApplicationService {
    private final PageApplicationRepository pageApplicationRepository;

    public PageApplicationService(PageApplicationRepository pageApplicationRepository) {
        this.pageApplicationRepository = pageApplicationRepository;
    }

    public void savePageApplication(PageApplication pageApplication) {
        pageApplicationRepository.save(pageApplication);
    }
    public PageApplication getPageApplication(Long id){
        Optional<PageApplication> pageApplicationOptional = pageApplicationRepository.findById(id);
        if(pageApplicationOptional.isPresent()){
            return pageApplicationOptional.get();
        } else throw new RuntimeException("Error base in method getPageApplication() --> 'PageApplicationService.class' ");
    }
    public List<PageApplication> pageApplicationList(){
        return (List<PageApplication>)pageApplicationRepository.findAll();
    }
}
