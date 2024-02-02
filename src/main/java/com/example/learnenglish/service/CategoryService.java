package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
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

    public Category getCategory(long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();

        } else throw new ObjectNotFoundException("Category with id: " + id + "not found");
    }

    public Category getCategoryByUIID(String uiid) {
        Optional<Category> categoryOptional = categoryRepository.findCategoriesByUuid(uiid);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();

        } else throw new ObjectNotFoundException("Category with id: " + uiid + "not found");
    }
    public Category getNewCategory(long id) {
        Category category = new Category();
        category.setId(id);
        category.setName("Enter new name category");
        category.setDescription("Enter new info category");
        return category;
    }

    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();

    }

    public long countCategory() {
        return categoryRepository.lastId();
    }

    public List<Category> mainCategoryList(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryOrderByNameAsc(mainCategory);
    }

    public List<Category> getMainCategoryListByCategoryPage(boolean mainCategory, CategoryPage categoryPage) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(mainCategory, categoryPage);
    }

    public List<Category> mainTranslationPairsCategoryListUser(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByIdAsc(mainCategory, CategoryPage.MINI_STORIES);
    }

    public List<Category> getSubcategoriesFromMainCategory(long id) {
        return categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
    }

    public List<Category> getSubcategoriesPhraseLesson() {
        List<Category> phraseLessonMainCategories = getMainCategoryListByCategoryPage(true, CategoryPage.LESSON_PHRASES);
        List<Category> subcategories = new ArrayList<>();
        for (Category arr: phraseLessonMainCategories) {
            subcategories.addAll(arr.getSubcategories());
        }
        return subcategories;
    }

    public CustomResponseMessage saveMainCategory(Category category, Category categoryDb) {
        categoryDb.setName(category.getName());
        categoryDb.setDescription(category.getDescription());
        categoryDb.setMainCategory(true);
        categoryDb.setViewSubcategoryFullNoInfoOrNameAndInfo(category.isViewSubcategoryFullNoInfoOrNameAndInfo());

        List<CategoryPage> categoryPages = new ArrayList<>(category.getCategoryPages());
        if (!categoryPages.isEmpty() && !categoryPages.get(0).equals(CategoryPage.NO_PAGE)) {
            categoryDb.getCategoryPages().clear();
            categoryDb.setCategoryPages(category.getCategoryPages());
        }
        if (categoryDb.getParentCategory() != null) {
            Category parentCategory = categoryDb.getParentCategory();
            parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(categoryDb.getId()));
        }
        if(category.getImage().getImageName() != null) categoryDb.getImage().setImageName(category.getImage().getImageName());
        categoryDb.setParentCategory(null);
        categoryRepository.save(categoryDb);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }


    public CustomResponseMessage saveSubcategory(Category category, Category categoryDb) {
        categoryDb.setName(category.getName());
        categoryDb.setDescription(category.getDescription());
        categoryDb.setMainCategory(false);
        categoryDb.getCategoryPages().clear();
        if (categoryDb.getParentCategory() == null || category.getParentCategory().getId() != 0 && !category.getParentCategory().getId().equals(categoryDb.getParentCategory().getId())) {
            categoryDb.setParentCategory(getCategory(category.getParentCategory().getId()));
        }
        if(category.getImage().getImageName() != null) categoryDb.getImage().setImageName(category.getImage().getImageName());
        categoryRepository.save(categoryDb);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public CustomResponseMessage saveNewCategory(Category category) {
        if(category.getParentCategory() != null && category.getParentCategory().getId() == 0) {
            category.setParentCategory(null);
        } else {
            Category parentCategory = getCategory(category.getParentCategory().getId());
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }
}
