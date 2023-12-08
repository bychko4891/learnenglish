package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPairsPage;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.PhraseUser;
import com.example.learnenglish.model.TranslationPairsPage;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.TranslationPairPageRepository;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
// Буде змінюватись
@Service
@RequiredArgsConstructor
public class TranslationPairPageService {

    private final TranslationPairPageRepository translationPairPageRepository;
    private final PhraseUserRepository phraseUserRepository;
    private final CategoryRepository categoryRepository;


    public Page<TranslationPairsPage> getTranslationPairsPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return translationPairPageRepository.findAll(pageable);
    }
    public Page<TranslationPairsPage> getTranslationPairsPagesToUser(int page, int size, Long id) {
        Pageable pageable = PageRequest.of(page, size);
        return translationPairPageRepository.findAllToUser(pageable, id);
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


    public CustomResponseMessage saveTranslationPairsPage(DtoTranslationPairsPage dtoTranslationPairsPage) {
        Optional<TranslationPairsPage> dtoTranslationPairsPageOptional = translationPairPageRepository.findById(dtoTranslationPairsPage.getTranslationPairsPage().getId());
        Long categoryId = dtoTranslationPairsPage.getSubSubcategorySelect().getId() != 0 ? dtoTranslationPairsPage.getSubSubcategorySelect().getId() :
                            dtoTranslationPairsPage.getSubcategorySelect().getId() != 0 ? dtoTranslationPairsPage.getSubcategorySelect().getId() :
                            dtoTranslationPairsPage.getMainCategorySelect().getId() != 0 ? dtoTranslationPairsPage.getMainCategorySelect().getId() : 0;
        if (dtoTranslationPairsPageOptional.isPresent()) {
            TranslationPairsPage translationPairsPage = dtoTranslationPairsPageOptional.get();
            translationPairsPage.setName(dtoTranslationPairsPage.getTranslationPairsPage().getName());
            translationPairsPage.setPublished(dtoTranslationPairsPage.getTranslationPairsPage().isPublished());
            translationPairsPage.setInfo(dtoTranslationPairsPage.getTranslationPairsPage().getInfo());
            List<PhraseUser> phraseUsers = translationPairsPage.getPhraseUsers();
            if (translationPairsPage.getPhraseUsers().size() != 0 &&
                    translationPairsPage.getPhraseUsers().size() != dtoTranslationPairsPage.getTranslationPairsPage().getPhraseUsers().size()) {
                List<PhraseUser> dtoPhraseUsers = dtoTranslationPairsPage.getTranslationPairsPage().getPhraseUsers();
                Iterator<PhraseUser> iterator = phraseUsers.iterator();
                while (iterator.hasNext()) {
                    PhraseUser pair = iterator.next();
                    boolean containsId = false;
                    for (PhraseUser arr : dtoPhraseUsers) {
                        if (pair.getId().equals(arr.getId())) {
                            containsId = true;
                            break;
                        }
                    }
                    if (!containsId) {
                        iterator.remove();
                    }
                }
            }
        if (dtoTranslationPairsPage.getTranslationPairsId().size() != 0) {
            List<PhraseUser> list = phraseUserRepository.findByIds(dtoTranslationPairsPage.getTranslationPairsId());
            for (PhraseUser arr : list) {
                translationPairsPage.getPhraseUsers().add(arr);
            }
        }
            translationPairsPage.setPhraseUsers(phraseUsers);
        if (categoryId != 0 && translationPairsPage.getTranslationPairsPageCategory() == null) {
            Category category = categoryRepository.findById(categoryId).get();
            translationPairsPage.setTranslationPairsPageCategory(category);
            category.getTranslationPairsPages().add(translationPairsPage);

        } else if (categoryId != 0 && !translationPairsPage.getTranslationPairsPageCategory().getId().equals(categoryId)) {
            Category categoryRemove = translationPairsPage.getTranslationPairsPageCategory();
            categoryRemove.getWords().removeIf(obj -> obj.getId().equals(translationPairsPage.getId()));
            translationPairsPage.setTranslationPairsPageCategory(categoryRepository.findById(categoryId).get());
        }
            translationPairPageRepository.save(translationPairsPage);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    } else return saveNewTranslationPairsPage(dtoTranslationPairsPage, categoryId);
}

    private CustomResponseMessage saveNewTranslationPairsPage(DtoTranslationPairsPage dtoTranslationPairsPage, Long categoryId) {
        TranslationPairsPage translationPairsPage = new TranslationPairsPage();
        translationPairsPage.setName(dtoTranslationPairsPage.getTranslationPairsPage().getName());
        translationPairsPage.setPublished(dtoTranslationPairsPage.getTranslationPairsPage().isPublished());
        translationPairsPage.setInfo(dtoTranslationPairsPage.getTranslationPairsPage().getInfo());
        if (dtoTranslationPairsPage.getTranslationPairsId().size() != 0) {
            List<PhraseUser> list = phraseUserRepository.findByIds(dtoTranslationPairsPage.getTranslationPairsId());
            translationPairsPage.setPhraseUsers(list);
        }
        if (categoryId != 0) {
            Category category = categoryRepository.findById(categoryId).get();
            translationPairsPage.setTranslationPairsPageCategory(category);
            category.getTranslationPairsPages().add(translationPairsPage);
        }
        translationPairPageRepository.save(translationPairsPage);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

}