package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTranslationPairsPage;
import com.example.learnenglish.model.MiniStory;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.MiniStoryRepository;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
// Буде змінюватись
@Service
@RequiredArgsConstructor
public class MiniStoryService {

    private final MiniStoryRepository miniStoryRepository;
    private final PhraseUserRepository phraseUserRepository;
    private final CategoryRepository categoryRepository;


    public Page<MiniStory> getTranslationPairsPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return miniStoryRepository.findAll(pageable);
    }
    public Page<MiniStory> getTranslationPairsPagesToUser(int page, int size, Long id) {
        Pageable pageable = PageRequest.of(page, size);
        return miniStoryRepository.findAllToUser(pageable, id);
    }

    public Long countTranslationPairPages() {
        return miniStoryRepository.count();
    }

    public MiniStory getTranslationPairsPage(Long id) {
        Optional<MiniStory> translationPairsPageOptional = miniStoryRepository.findById(id);
        if (translationPairsPageOptional.isPresent()) {
            return translationPairsPageOptional.get();
        } else
            throw new RuntimeException("Error in method 'getTranslationPairsPage' in class 'TranslationPairPageService'");
    }


    public CustomResponseMessage saveTranslationPairsPage(DtoTranslationPairsPage dtoTranslationPairsPage) {
//        Optional<MiniStory> dtoTranslationPairsPageOptional = translationPairPageRepository.findById(dtoTranslationPairsPage.getMiniStory().getId());
//        Long categoryId = dtoTranslationPairsPage.getSubSubcategorySelect().getId() != 0 ? dtoTranslationPairsPage.getSubSubcategorySelect().getId() :
//                            dtoTranslationPairsPage.getSubcategorySelect().getId() != 0 ? dtoTranslationPairsPage.getSubcategorySelect().getId() :
//                            dtoTranslationPairsPage.getMainCategorySelect().getId() != 0 ? dtoTranslationPairsPage.getMainCategorySelect().getId() : 0;
//        if (dtoTranslationPairsPageOptional.isPresent()) {
//            MiniStory miniStory = dtoTranslationPairsPageOptional.get();
//            miniStory.setName(dtoTranslationPairsPage.getMiniStory().getName());
//            miniStory.setPublished(dtoTranslationPairsPage.getMiniStory().isPublished());
//            miniStory.setStory(dtoTranslationPairsPage.getMiniStory().getStory());
//            List<PhraseUser> phraseUsers = miniStory.getPhraseUsers();
//            if (miniStory.getPhraseUsers().size() != 0 &&
//                    miniStory.getPhraseUsers().size() != dtoTranslationPairsPage.getMiniStory().getPhraseUsers().size()) {
//                List<PhraseUser> dtoPhraseUsers = dtoTranslationPairsPage.getMiniStory().getPhraseUsers();
//                Iterator<PhraseUser> iterator = phraseUsers.iterator();
//                while (iterator.hasNext()) {
//                    PhraseUser pair = iterator.next();
//                    boolean containsId = false;
//                    for (PhraseUser arr : dtoPhraseUsers) {
//                        if (pair.getId().equals(arr.getId())) {
//                            containsId = true;
//                            break;
//                        }
//                    }
//                    if (!containsId) {
//                        iterator.remove();
//                    }
//                }
//            }
//        if (dtoTranslationPairsPage.getTranslationPairsId().size() != 0) {
//            List<PhraseUser> list = phraseUserRepository.findByIds(dtoTranslationPairsPage.getTranslationPairsId());
//            for (PhraseUser arr : list) {
//                miniStory.getPhraseUsers().add(arr);
//            }
//        }
//            miniStory.setPhraseUsers(phraseUsers);
//        if (categoryId != 0 && miniStory.getCategory() == null) {
//            Category category = categoryRepository.findById(categoryId).get();
//            miniStory.setCategory(category);
//            category.getTranslationPairsPages().add(miniStory);
//
//        } else if (categoryId != 0 && !miniStory.getCategory().getId().equals(categoryId)) {
//            Category categoryRemove = miniStory.getCategory();
//            categoryRemove.getWords().removeIf(obj -> obj.getId().equals(miniStory.getId()));
//            miniStory.setCategory(categoryRepository.findById(categoryId).get());
//        }
//            translationPairPageRepository.save(miniStory);
//        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
//    } else return saveNewTranslationPairsPage(dtoTranslationPairsPage, categoryId);
        return null;
}

    private CustomResponseMessage saveNewTranslationPairsPage(DtoTranslationPairsPage dtoTranslationPairsPage, Long categoryId) {
//        MiniStory miniStory = new MiniStory();
//        miniStory.setName(dtoTranslationPairsPage.getMiniStory().getName());
//        miniStory.setPublished(dtoTranslationPairsPage.getMiniStory().isPublished());
//        miniStory.setStory(dtoTranslationPairsPage.getMiniStory().getStory());
//        if (dtoTranslationPairsPage.getTranslationPairsId().size() != 0) {
//            List<PhraseUser> list = phraseUserRepository.findByIds(dtoTranslationPairsPage.getTranslationPairsId());
//            miniStory.setPhraseUsers(list);
//        }
//        if (categoryId != 0) {
//            Category category = categoryRepository.findById(categoryId).get();
//            miniStory.setCategory(category);
//            category.getTranslationPairsPages().add(miniStory);
//        }
//        translationPairPageRepository.save(miniStory);
//        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        return null;
    }

}