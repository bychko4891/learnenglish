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
    private final WordCategoryRepository wordsCategoryRepository;

    public WordCategoryService(WordCategoryRepository wordsCategoryRepository) {
        this.wordsCategoryRepository = wordsCategoryRepository;
    }

    public WordCategory getWordCategoryToEditor(Long id) {
        Optional<WordCategory> wordCategoryOptional = wordsCategoryRepository.findById(id);
        if (wordCategoryOptional.isPresent()) {
            return wordCategoryOptional.get();
        }
        throw new RuntimeException("Error base in method 'getWordCategory' class 'WordCategoryService'");
    }

    public List<DtoWordsCategoryToUi> getWordsCategories() {
        List<WordCategory> wordCategories = (List<WordCategory>) wordsCategoryRepository.findAll();
        List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
        for (WordCategory wordCategory : wordCategories) {
            dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.fromEntity(wordCategory));
        }
        return dtoWordsCategoryToUiList;
    }

    public Long countWordCategory() {
        return wordsCategoryRepository.count();
    }

    public List<WordCategory> mainWordCategoryList(boolean mainCategory) {
        return wordsCategoryRepository.findWordCategoriesByMainCategory(mainCategory);
    }

    public List<DtoWordsCategoryToUi> getSubcategoriesInMainCategory(Long id) {
        Optional<WordCategory> wordCategoryOption = wordsCategoryRepository.findById(id);
        if (wordCategoryOption.isPresent()) {
            List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
            for (WordCategory arr : wordCategoryOption.get().getSubcategories()) {
                dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.subcategoriesEditorConvertToDto(arr));
            }
            return dtoWordsCategoryToUiList;
        }
        throw new IllegalArgumentException("Main category with id " + id + " not found");
    }
    public List<WordCategory> getSubcategoriesAndSubSubcategoriesInMainCategory(Long id) {
        Optional<WordCategory> wordCategoryOption = wordsCategoryRepository.findById(id);
        if (wordCategoryOption.isPresent()) {
            return wordCategoryOption.get().getSubcategories();
        }
        throw new IllegalArgumentException("Main category with id " + id + " not found");
    }

    public ResponseMessage saveWordCategory(DtoWordsCategory dtoWordsCategory) {
        Optional<WordCategory> wordCategoryOptional = wordsCategoryRepository.findById(dtoWordsCategory.getWordsCategory().getId());
        if (wordCategoryOptional.isPresent()) {
            WordCategory wordsCategory = wordCategoryOptional.get();
            if(wordsCategory.getId() == dtoWordsCategory.getMainCategorySelect().getId() ||
                    wordsCategory.getId() == dtoWordsCategory.getSubcategorySelect().getId())  return new ResponseMessage(Message.ERROR_SAVE_CATEGORY);
            wordsCategory.setName(dtoWordsCategory.getWordsCategory().getName());
            wordsCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
            wordsCategory.setViewSubcategoryFullNoInfoOrNameAndInfo(dtoWordsCategory.getWordsCategory().isViewSubcategoryFullNoInfoOrNameAndInfo());
            if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                wordsCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                if (dtoWordsCategory.getWordsCategory().isMainCategory() && wordsCategory.getParentCategory() != null) {
                    WordCategory parentCategory = wordsCategory.getParentCategory();
                    parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(wordsCategory.getId()));
                    wordsCategory.setParentCategory(null);
                }
                wordsCategoryRepository.save(wordsCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else if (dtoWordsCategory.getMainCategorySelect().getId() != 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
//                if(wordsCategory.getId() == dtoWordsCategory.getMainCategorySelect().getId()) return new ResponseMessage(Message.ERROR_SAVE_CATEGORY);
                WordCategory parentWordsCategory = wordsCategoryRepository.findById(dtoWordsCategory.getMainCategorySelect().getId()).get();
                if (wordsCategory.getParentCategory() != null) {
                    WordCategory parentWordsCategoryRemove = wordsCategory.getParentCategory();
                    parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(wordsCategory.getId()));
                }
                wordsCategory.setMainCategory(false);
                wordsCategory.setParentCategory(parentWordsCategory);
                parentWordsCategory.getSubcategories().add(wordsCategory);
                wordsCategoryRepository.save(wordsCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
//                if(wordsCategory.getId() == dtoWordsCategory.getSubcategorySelect().getId()) return new ResponseMessage(Message.ERROR_SAVE_CATEGORY);
                WordCategory parentWordsCategory = wordsCategoryRepository.findById(dtoWordsCategory.getSubcategorySelect().getId()).get();
                if (wordsCategory.getParentCategory() != null) {
                    WordCategory parentWordsCategoryRemove = wordsCategory.getParentCategory();
                    parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(wordsCategory.getId()));
                }
                wordsCategory.setMainCategory(false);
                wordsCategory.setParentCategory(parentWordsCategory);
                parentWordsCategory.getSubcategories().add(wordsCategory);
                wordsCategoryRepository.save(wordsCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            }
        }
        return saveNewWordsCategory(dtoWordsCategory);
    }

    private ResponseMessage saveNewWordsCategory(DtoWordsCategory dtoWordsCategory) {
        WordCategory wordCategory = new WordCategory();
        if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
            wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
            wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
            wordCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
            wordsCategoryRepository.save(wordCategory);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else if (dtoWordsCategory.getMainCategorySelect().getId() != 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
            WordCategory parentWordCategory = wordsCategoryRepository.findById(dtoWordsCategory.getMainCategorySelect().getId()).get();
            wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
            wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
            wordCategory.setMainCategory(false);
            parentWordCategory.getSubcategories().add(wordCategory);
            wordCategory.setParentCategory(parentWordCategory);
            wordsCategoryRepository.save(wordCategory);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else {
            WordCategory parentWordCategory = wordsCategoryRepository.findById(dtoWordsCategory.getSubcategorySelect().getId()).get();
            wordCategory.setName(dtoWordsCategory.getWordsCategory().getName());
            wordCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
            wordCategory.setMainCategory(false);
            parentWordCategory.getSubcategories().add(wordCategory);
            wordCategory.setParentCategory(parentWordCategory);
            wordsCategoryRepository.save(wordCategory);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        }
    }
}
