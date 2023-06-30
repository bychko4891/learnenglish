package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoWordsCategory;
import com.example.learnenglish.dto.DtoWordsCategoryToUi;
import com.example.learnenglish.model.WordCategory;
import com.example.learnenglish.repository.WordCategoryRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordCategoryService {
    private final WordCategoryRepository wordCategoryRepository;

    public WordCategoryService(WordCategoryRepository wordCategoryRepository) {
        this.wordCategoryRepository = wordCategoryRepository;
    }

    public WordCategory getWordCategory(Long id) {
        Optional<WordCategory> wordCategoryOptional = wordCategoryRepository.findById(id);
        if (wordCategoryOptional.isPresent()) {
            return wordCategoryOptional.get();
        }
        throw new RuntimeException("Error base in method 'getWordCategory' class 'WordCategoryService'");
    }

    public List<DtoWordsCategoryToUi> getWordsCategories() {
        List<WordCategory> wordCategories = (List<WordCategory>) wordCategoryRepository.findAll();
        List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
        for (WordCategory wordCategory : wordCategories) {
            dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.fromEntity(wordCategory));
        }
        return dtoWordsCategoryToUiList;
    }

    public Long countWordCategory() {
        return wordCategoryRepository.count();
    }

    public List<WordCategory> mainWordCategoryList(boolean mainCategory) {
        return wordCategoryRepository.findWordCategoriesByMainCategory(mainCategory);
    }

    public List<DtoWordsCategoryToUi> getSubcategoriesInMainCategory(Long id) {
        Optional<WordCategory> wordCategoryOption = wordCategoryRepository.findById(id);
        if (wordCategoryOption.isPresent()) {
            List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
            for (WordCategory arr : wordCategoryOption.get().getSubcategories()) {
                dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.subcategoriesEditorConvertToDto(arr));
            }
            return dtoWordsCategoryToUiList ;
        }
        throw new IllegalArgumentException("Main category with id " + id + " not found");
    }

    public ResponseMessage saveWordCategory(DtoWordsCategory dtoWordsCategory) {
        Optional<WordCategory> wordCategoryOptional = wordCategoryRepository.findById(dtoWordsCategory.getWordsCategory().getId());
        if (wordCategoryOptional.isPresent()) {
            WordCategory wordCategory = wordCategoryOptional.get();
            if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
                wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
                wordCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                if(wordCategory.getParentCategory() != null){
                    wordCategory.setMainCategory(false);
                } else if(wordCategory.getParentCategory() == null && wordCategory.getSubcategories().size() > 0){
                    wordCategory.setMainCategory(true);
                }
                wordCategoryRepository.save(wordCategory);
            }
        } else {
            WordCategory wordCategory = new WordCategory();
            if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
                wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
                wordCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                wordCategoryRepository.save(wordCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else if (dtoWordsCategory.getMainCategorySelect().getId() != 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                WordCategory parentWordCategory = wordCategoryRepository.findById(dtoWordsCategory.getMainCategorySelect().getId()).get();
                wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
                wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
                wordCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                parentWordCategory.getSubcategories().add(wordCategory);
                wordCategory.setParentCategory(parentWordCategory);
                wordCategoryRepository.save(wordCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
                WordCategory parentWordCategory = wordCategoryRepository.findById(dtoWordsCategory.getSubcategorySelect().getId()).get();
                wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
                wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
                wordCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                parentWordCategory.getSubcategories().add(wordCategory);
                wordCategory.setParentCategory(parentWordCategory);
                wordCategoryRepository.save(wordCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            }
        }
        return null;
    }

}
