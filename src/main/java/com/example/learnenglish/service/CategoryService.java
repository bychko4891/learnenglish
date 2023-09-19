package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoCategory;
import com.example.learnenglish.dto.DtoWordsCategoryToUi;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import org.springframework.stereotype.Service;

import java.util.*;
// Буде змінюватись
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
        List<Category> subcategories = categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
        List<DtoWordsCategoryToUi> dtoWordsCategoryToUiList = new ArrayList<>();
        for (Category arr : subcategories) {
            dtoWordsCategoryToUiList.add(DtoWordsCategoryToUi.subcategoriesEditorConvertToDto(arr));
        }
        return dtoWordsCategoryToUiList;
    }

    public List<Category> getSubcategoriesAndSubSubcategoriesInMainCategory(Long id) {
        return categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
    }

    public CustomResponseMessage saveCategory(DtoCategory dtoCategory, Category сategory) {
        сategory.setName(dtoCategory.getMainCategorySelect().getName());
        сategory.setInfo(dtoCategory.getMainCategorySelect().getInfo());
        сategory.setViewSubcategoryFullNoInfoOrNameAndInfo(dtoCategory.getMainCategorySelect().isViewSubcategoryFullNoInfoOrNameAndInfo());
        String page = dtoCategory.getMainCategorySelect().getCategoryPages().toString();
        if (!page.equals("[NO_PAGE]")) {
            if (page.equals("[WORDS]")) {
                сategory.getCategoryPages().clear();
                сategory.getCategoryPages().add(CategoryPage.WORDS);
            } else if (page.equals("[LESSON_WORDS]")) {
                сategory.getCategoryPages().clear();
                сategory.getCategoryPages().add(CategoryPage.LESSON_WORDS);
            } else {
                сategory.getCategoryPages().clear();
                сategory.getCategoryPages().add(CategoryPage.TRANSLATION_PAIRS);
            }
        }
        if (dtoCategory.getSubcategorySelect().getId() == 0 && dtoCategory.getSubSubcategorySelect().getId() == 0) {
            сategory.setMainCategory(dtoCategory.getMainCategorySelect().isMainCategory());
            if (dtoCategory.getMainCategorySelect().isMainCategory() && сategory.getParentCategory() != null) {
                Category parentCategory = сategory.getParentCategory();
                parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
                сategory.setParentCategory(null);
            }
            categoryRepository.save(сategory);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else if (dtoCategory.getSubcategorySelect().getId() != 0 && dtoCategory.getSubSubcategorySelect().getId() == 0) {
            Category parentWordsCategory = categoryRepository.findById(dtoCategory.getSubcategorySelect().getId()).get();
            if (сategory.getParentCategory() != null) {
                Category parentWordsCategoryRemove = сategory.getParentCategory();
                parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
            }
            сategory.setMainCategory(false);
            сategory.setParentCategory(parentWordsCategory);
            parentWordsCategory.getSubcategories().add(сategory);
            categoryRepository.save(сategory);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else {
            Category parentWordsCategory = categoryRepository.findById(dtoCategory.getSubSubcategorySelect().getId()).get();
            if (сategory.getParentCategory() != null) {
                Category parentWordsCategoryRemove = сategory.getParentCategory();
                parentWordsCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
            }
            сategory.setMainCategory(false);
            сategory.setParentCategory(parentWordsCategory);
            parentWordsCategory.getSubcategories().add(сategory);
            categoryRepository.save(сategory);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        }
    }

    public CustomResponseMessage saveNewCategory(DtoCategory dtoCategory) {
        Category category = new Category();
        Image image = new Image();
        category.setName(dtoCategory.getMainCategorySelect().getName());
        category.setInfo(dtoCategory.getMainCategorySelect().getInfo());
        category.setImage(image);
        if (dtoCategory.getSubcategorySelect().getId() == 0 && dtoCategory.getSubSubcategorySelect().getId() == 0) {
            category.setMainCategory(dtoCategory.getMainCategorySelect().isMainCategory());
            categoryRepository.save(category);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else if (dtoCategory.getSubcategorySelect().getId() != 0 && dtoCategory.getSubSubcategorySelect().getId() == 0) {
            Category parentWordCategory = categoryRepository.findById(dtoCategory.getSubcategorySelect().getId()).get();
            category.setMainCategory(false);
            parentWordCategory.getSubcategories().add(category);
            category.setParentCategory(parentWordCategory);
            categoryRepository.save(category);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else {
            Category parentWordCategory = categoryRepository.findById(dtoCategory.getSubSubcategorySelect().getId()).get();
            category.setMainCategory(false);
            parentWordCategory.getSubcategories().add(category);
            category.setParentCategory(parentWordCategory);
            categoryRepository.save(category);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        }
    }
}
