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
        } else {
            Category category = new Category();
            category.setId(id);
            category.setName("Enter new name category");
            category.setInfo("Enter new info category");
            return category;
        }
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
    public List<Category> mainWordLessonCategoryList(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(mainCategory, CategoryPage.LESSON_WORDS);
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
            Category сategory = categoryOptional.get();
            if(сategory.getId() == dtoWordsCategory.getMainCategorySelect().getId() ||
                    сategory.getId() == dtoWordsCategory.getSubcategorySelect().getId())  return new ResponseMessage(Message.ERROR_SAVE_CATEGORY);
            сategory.setName(dtoWordsCategory.getWordsCategory().getName());
            сategory.setInfo(dtoWordsCategory.getWordsCategory().getInfo());
            сategory.setViewSubcategoryFullNoInfoOrNameAndInfo(dtoWordsCategory.getWordsCategory().isViewSubcategoryFullNoInfoOrNameAndInfo());
            String page = dtoWordsCategory.getWordsCategory().getCategoryPages().toString();
            if(!page.equals("[NO_PAGE]")){
                if(page.equals("[WORDS]")){
                    сategory.getCategoryPages().clear();
                    сategory.getCategoryPages().add(CategoryPage.WORDS);
                } else if(page.equals("[LESSON_WORDS]")){
                    сategory.getCategoryPages().clear();
                    сategory.getCategoryPages().add(CategoryPage.LESSON_WORDS);
                }else {
                    сategory.getCategoryPages().clear();
                    сategory.getCategoryPages().add(CategoryPage.TRANSLATION_PAIRS);
                }
            }
            if (dtoWordsCategory.getMainCategorySelect().getId() == 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                сategory.setMainCategory(dtoWordsCategory.getWordsCategory().isMainCategory());
                if (dtoWordsCategory.getWordsCategory().isMainCategory() && сategory.getParentCategory() != null) {
                    Category parentCategory = сategory.getParentCategory();
                    parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
                    сategory.setParentCategory(null);
                }
                categoryRepository.save(сategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else if (dtoWordsCategory.getMainCategorySelect().getId() != 0 && dtoWordsCategory.getSubcategorySelect().getId() == 0) {
                Category parentWordsCategory = categoryRepository.findById(dtoWordsCategory.getMainCategorySelect().getId()).get();
                if (сategory.getParentCategory() != null) {
                    Category parentWordsCategoryRemove = сategory.getParentCategory();
                    parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
                }
                сategory.setMainCategory(false);
                сategory.setParentCategory(parentWordsCategory);
                parentWordsCategory.getSubcategories().add(сategory);
                categoryRepository.save(сategory);
                return new ResponseMessage(Message.SUCCESSADDBASE);
            } else {
                Category parentWordsCategory = categoryRepository.findById(dtoWordsCategory.getSubcategorySelect().getId()).get();
                if (сategory.getParentCategory() != null) {
                    Category parentWordsCategoryRemove = сategory.getParentCategory();
                    parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
                }
                сategory.setMainCategory(false);
                сategory.setParentCategory(parentWordsCategory);
                parentWordsCategory.getSubcategories().add(сategory);
                categoryRepository.save(сategory);
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
