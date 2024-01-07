package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.repository.PageApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageApplicationService {
    private final PageApplicationRepository pageApplicationRepository;

    public PageApplicationService(PageApplicationRepository pageApplicationRepository) {
        this.pageApplicationRepository = pageApplicationRepository;
    }

    public void savePageApplication(PageApplication pageApplication) {
        pageApplicationRepository.save(pageApplication);
    }

    public PageApplication getPageApplication(long id) {
        Optional<PageApplication> pageApplicationOptional = pageApplicationRepository.findById(id);
        if (pageApplicationOptional.isPresent()) {
            return pageApplicationOptional.get();
        } else
            throw new RuntimeException("Error base in method getPageApplication() --> 'PageApplicationService.class' ");
    }

    public List<PageApplication> pageApplicationList() {
        return (List<PageApplication>) pageApplicationRepository.findAll();
    }
}
