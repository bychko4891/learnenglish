package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoCategoryFromEditor;
import com.example.learnenglish.dto.DtoWordsCategoryToUi;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
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

    public Long countCategory() {
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

    public CustomResponseMessage saveMainCategory(Category category, Category categoryFromDatabase) {
        categoryFromDatabase.setName(category.getName());
        categoryFromDatabase.setInfo(category.getInfo());
        categoryFromDatabase.setMainCategory(true);
        categoryFromDatabase.setViewSubcategoryFullNoInfoOrNameAndInfo(category.isViewSubcategoryFullNoInfoOrNameAndInfo());
        String page = category.getCategoryPages().get(0).name();
        if (!page.equals("NO_PAGE")) {
            categoryFromDatabase.getCategoryPages().clear();
            categoryFromDatabase.getCategoryPages().add(category.getCategoryPages().get(0));
        }
        if (categoryFromDatabase.getParentCategory() != null) {
            Category parentCategory = categoryFromDatabase.getParentCategory();
            parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(categoryFromDatabase.getId()));
        }
        categoryFromDatabase.setParentCategory(null);
        categoryRepository.save(categoryFromDatabase);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }


    public CustomResponseMessage saveSubcategory(DtoCategoryFromEditor dtoCategory, Category сategory) {
        сategory.setName(dtoCategory.getCategory().getName());
        сategory.setInfo(dtoCategory.getCategory().getInfo());
        сategory.setMainCategory(false);
        if (dtoCategory.getMainCategoryId() != 0) {
            Category parentCategory = null;
            if (dtoCategory.getSubcategoryId() != 0) {
                parentCategory = categoryRepository.findById(dtoCategory.getSubcategoryId()).get();
            } else {
                parentCategory = categoryRepository.findById(dtoCategory.getMainCategoryId()).get();
            }
            сategory.setParentCategory(parentCategory);
            parentCategory.getSubcategories().add(сategory);
        }
        if (сategory.getParentCategory() != null) {
            Category parentCategoryRemove = сategory.getParentCategory();
            parentCategoryRemove.getSubcategories().removeIf(obj -> obj.getId().equals(сategory.getId()));
        }
        categoryRepository.save(сategory);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public CustomResponseMessage saveNewCategory(DtoCategoryFromEditor dtoCategory) {
        Category category = new Category();
        Image image = new Image();
        category.setName(dtoCategory.getCategory().getName());
        category.setInfo(dtoCategory.getCategory().getInfo());
        category.setImage(image);
        if (dtoCategory.getMainCategoryId() == 0 && dtoCategory.getSubcategoryId() == 0) {
            category.setMainCategory(dtoCategory.getCategory().isMainCategory());
            categoryRepository.save(category);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else if (dtoCategory.getMainCategoryId() != 0 && dtoCategory.getSubcategoryId() == 0) {
            Category parentWordCategory = categoryRepository.findById(dtoCategory.getMainCategoryId()).get();
            category.setMainCategory(false);
            parentWordCategory.getSubcategories().add(category);
            category.setParentCategory(parentWordCategory);
            categoryRepository.save(category);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else {
            Category parentWordCategory = categoryRepository.findById(dtoCategory.getSubcategoryId()).get();
            category.setMainCategory(false);
            parentWordCategory.getSubcategories().add(category);
            category.setParentCategory(parentWordCategory);
            categoryRepository.save(category);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        }
    }
}
