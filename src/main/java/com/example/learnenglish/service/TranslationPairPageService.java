package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPairsPage;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.TranslationPairsPage;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.TranslationPairPageRepository;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TranslationPairPageService {

    private final TranslationPairPageRepository translationPairPageRepository;
    private final TranslationPairRepository translationPairRepository;
    private final CategoryRepository categoryRepository;

    public TranslationPairPageService(TranslationPairPageRepository translationPairPageRepository, TranslationPairRepository translationPairRepository, CategoryRepository categoryRepository) {
        this.translationPairPageRepository = translationPairPageRepository;
        this.translationPairRepository = translationPairRepository;
        this.categoryRepository = categoryRepository;
    }


    public Page<TranslationPairsPage> getTranslationPairsPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return translationPairPageRepository.findAll(pageable);
    }

    public Long countTranslationPairPages() {
        return translationPairPageRepository.count();
    }

    public TranslationPairsPage getTranslationPairsPage(Long id) {
        Optional<TranslationPairsPage> translationPairsPageOptional = translationPairPageRepository.findById(id);
        if (translationPairsPageOptional.isPresent()) {
            return translationPairsPageOptional.get();
        } else
            throw new RuntimeException("Error in method 'getTranslationPairsPage' in class 'TranslationPairPageService'");
    }


    public ResponseMessage saveTranslationPairsPage(DtoTranslationPairsPage dtoTranslationPairsPage) {
        Optional<TranslationPairsPage> dtoTranslationPairsPageOptional = translationPairPageRepository.findById(dtoTranslationPairsPage.getTranslationPairsPage().getId());
        Long categoryId = dtoTranslationPairsPage.getSubSubcategorySelect().getId() != 0 ? dtoTranslationPairsPage.getSubSubcategorySelect().getId() :
                dtoTranslationPairsPage.getSubcategorySelect().getId() != 0 ? dtoTranslationPairsPage.getSubcategorySelect().getId() :
                        dtoTranslationPairsPage.getMainCategorySelect().getId() != 0 ? dtoTranslationPairsPage.getMainCategorySelect().getId() : 0;
        if (dtoTranslationPairsPageOptional.isPresent()) {
            TranslationPairsPage translationPairsPage = dtoTranslationPairsPageOptional.get();
            translationPairsPage.setName(dtoTranslationPairsPage.getTranslationPairsPage().getName());
            translationPairsPage.setPublished(dtoTranslationPairsPage.getTranslationPairsPage().isPublished());
            translationPairsPage.setInfo(dtoTranslationPairsPage.getTranslationPairsPage().getInfo());
            List<TranslationPair> translationPairs = translationPairsPage.getTranslationPairs();
            if (translationPairsPage.getTranslationPairs().size() != 0 &&
                    translationPairsPage.getTranslationPairs().size() != dtoTranslationPairsPage.getTranslationPairsPage().getTranslationPairs().size()) {

                List<TranslationPair> dtoTranslationPairs = dtoTranslationPairsPage.getTranslationPairsPage().getTranslationPairs();
                Iterator<TranslationPair> iterator = translationPairs.iterator();
                while (iterator.hasNext()) {
                    TranslationPair pair = iterator.next();
                    boolean containsId = false;
                    for (TranslationPair arr : dtoTranslationPairs) {
                        if (pair.getId() == arr.getId()) {
                            containsId = true;
                            break;
                        }
                    }
                    if (!containsId) {
                        iterator.remove();
                        pair.setTranslationPairsPage(null);
                    }
                }
                translationPairsPage.setTranslationPairs(translationPairs);
            }




        if (dtoTranslationPairsPage.getTranslationPairsId().size() != 0) {
            List<TranslationPair> list = translationPairRepository.findByIds(dtoTranslationPairsPage.getTranslationPairsId());
            for (TranslationPair arr : list) {
                arr.setTranslationPairsPage(translationPairsPage);
//                if(translationPairs == null) translationPairs = new ArrayList<>();
                translationPairs.add(arr);
            }
            translationPairsPage.setTranslationPairs(translationPairs);
        }
        if (categoryId != 0 && translationPairsPage.getTranslationPairsPageCategory() == null) {
            Category category = categoryRepository.findById(categoryId).get();
            translationPairsPage.setTranslationPairsPageCategory(category);
            category.getTranslationPairsPages().add(translationPairsPage);

        } else if (categoryId != 0 && translationPairsPage.getTranslationPairsPageCategory().getId() != categoryId) {
            Category categoryRemove = translationPairsPage.getTranslationPairsPageCategory();
            categoryRemove.getWords().removeIf(obj -> obj.getId().equals(translationPairsPage.getId()));
            translationPairsPage.setTranslationPairsPageCategory(categoryRepository.findById(categoryId).get());
        }
            translationPairPageRepository.save(translationPairsPage);
        return new ResponseMessage(Message.SUCCESSADDBASE);
    } else return

    saveNewTranslationPairsPage(dtoTranslationPairsPage, categoryId);

}

    private ResponseMessage saveNewTranslationPairsPage(DtoTranslationPairsPage dtoTranslationPairsPage, Long categoryId) {
        TranslationPairsPage translationPairsPage = new TranslationPairsPage();
        translationPairsPage.setName(dtoTranslationPairsPage.getTranslationPairsPage().getName());
        translationPairsPage.setPublished(dtoTranslationPairsPage.getTranslationPairsPage().isPublished());
        translationPairsPage.setInfo(dtoTranslationPairsPage.getTranslationPairsPage().getInfo());
        if (dtoTranslationPairsPage.getTranslationPairsId().size() != 0) {
            List<TranslationPair> list = translationPairRepository.findByIds(dtoTranslationPairsPage.getTranslationPairsId());
            for (TranslationPair arr : list) {
                arr.setTranslationPairsPage(translationPairsPage);
            }
            translationPairsPage.setTranslationPairs(list);
        }
        if (categoryId != 0) {
            Category category = categoryRepository.findById(categoryId).get();
            translationPairsPage.setTranslationPairsPageCategory(category);
            category.getTranslationPairsPages().add(translationPairsPage);
        }
        translationPairPageRepository.save(translationPairsPage);
        return new ResponseMessage(Message.SUCCESSADDBASE);
    }
}
