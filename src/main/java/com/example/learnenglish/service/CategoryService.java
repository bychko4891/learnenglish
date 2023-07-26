package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoWordsCategory;
import com.example.learnenglish.dto.DtoWordsCategoryToUi;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryToEditor(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        throw new RuntimeException("Error base in method 'getWordCategory' class 'CategoryService'");
    }

    public List<DtoWordsCategoryToUi> getWordsCategories() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
        for (Category wordCategory : categories) {
            dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.fromEntity(wordCategory));
        }
        return dtoWordsCategoryToUiList;
    }

    public Long countWordCategory() {
        return categoryRepository.lastId();
    }

    public List<Category> mainCategoryList(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryOrderByNameAsc(mainCategory);
    }
    public List<Category> mainWordCategoryList(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(mainCategory, CategoryPage.WORDS);
    }

    //               метод для Фраз            //
    public List<Category> mainTranslationPairsCategoryList(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(mainCategory, CategoryPage.TRANSLATION_PAIRS);
    }
    public List<Category> mainTranslationPairsCategoryListUser(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByIdAsc(mainCategory, CategoryPage.TRANSLATION_PAIRS);
    }

    public List<DtoWordsCategoryToUi> getDtoSubcategoriesInMainCategory(Long id) {
        List<Category> subcategories= categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
            List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
            for (Category arr : subcategories) {
                dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.subcategoriesEditorConvertToDto(arr));
            }
            return dtoWordsCategoryToUiList;
    }
    public List<Category> getSubcategoriesAndSubSubcategoriesInMainCategory(Long id) {
            return categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
    }

    public ResponseMessage saveCategory(DtoWordsCategory dtoWordsCategory) {
        Optional<Category> categoryOptional = categoryRepository.findById(dtoWordsCategory.getWordsCategory().getId());
        if (categoryOptional.isPresent()) {
            Category wordsCategory = categoryOptional.get();
            if(wordsCategory.getId() == dtoWordsCategory.getMainCategorySelect().getId() ||
                    wordsCategory.getId() == dtoWordsCategory.getSubcategorySelect().getId())  return new ResponseMessage(Message.ERROR_SAVE_CATEGORY);
            wordsCategory.setName(dtoWordsCategory.getWordsCategory().getName());
            wordsCategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
            wordsCategory.setViewSubcategoryFullNoInfoOrNameAndInfo(dtoWordsCategory.getWordsCategory().isViewSubcategoryFullNoInfoOrNameAndInfo());
            String page = dtoWordsCategory.getWordsCategory().getCategoryPages().toString();
            if(!page.equals("[NO_PAGE]")){
                if(page.equals("[WORDS]")){
                    wordsCategory.getCategoryPages().clear();
                    wordsCategory.getCategoryPages().add(CategoryPage.WORDS);
                } else {
                    wordsCategory.getCategoryPages().clear();
                    wordsCategory.getCategoryPages().add(CategoryPage.TRANSLATION_PAIRS);
                }
            }
            if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                wordsCategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                if (dtoWordsCategory.getWordsCategory().isMainCategory() && wordsCategory.getParentCategory() != null) {
                    Category parentCategory = wordsCategory.getParentCategory();
                    parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(wordsCategory.getId()));
                    wordsCategory.setParentCategory(null);
                }
                categoryRepository.save(wordsCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else if (dtoWordsCategory.getMainCategorySelect().getId() != 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                Category parentWordsCategory = categoryRepository.findById(dtoWordsCategory.getMainCategorySelect().getId()).get();
                if (wordsCategory.getParentCategory() != null) {
                    Category parentWordsCategoryRemove = wordsCategory.getParentCategory();
                    parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(wordsCategory.getId()));
                }
                wordsCategory.setMainCategory(false);
                wordsCategory.setParentCategory(parentWordsCategory);
                parentWordsCategory.getSubcategories().add(wordsCategory);
                categoryRepository.save(wordsCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
                Category parentWordsCategory = categoryRepository.findById(dtoWordsCategory.getSubcategorySelect().getId()).get();
                if (wordsCategory.getParentCategory() != null) {
                    Category parentWordsCategoryRemove = wordsCategory.getParentCategory();
                    parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(wordsCategory.getId()));
                }
                wordsCategory.setMainCategory(false);
                wordsCategory.setParentCategory(parentWordsCategory);
                parentWordsCategory.getSubcategories().add(wordsCategory);
                categoryRepository.save(wordsCategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            }
        }
        return saveNewCategory(dtoWordsCategory);
    }

    private ResponseMessage saveNewCategory(DtoWordsCategory dtoWordsCategory) {
        Category category = new Category();
        Image image = new Image();
        category.setName(dtoWordsCategory.getWordsCategory().getName());
        category.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
        category.setImage(image);
        if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
            category.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
            categoryRepository.save(category);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else if (dtoWordsCategory.getMainCategorySelect().getId() != 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
            Category parentWordCategory = categoryRepository.findById(dtoWordsCategory.getMainCategorySelect().getId()).get();
            category.setMainCategory(false);
            parentWordCategory.getSubcategories().add(category);
            category.setParentCategory(parentWordCategory);
            categoryRepository.save(category);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else {
            Category parentWordCategory = categoryRepository.findById(dtoWordsCategory.getSubcategorySelect().getId()).get();
            category.setMainCategory(false);
            parentWordCategory.getSubcategories().add(category);
            category.setParentCategory(parentWordCategory);
            categoryRepository.save(category);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        }
    }
}
