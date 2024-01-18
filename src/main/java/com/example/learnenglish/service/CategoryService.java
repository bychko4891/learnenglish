package com.example.learnenglish.service;

import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.model.Image;
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
        } throw new RuntimeException("Category with id: " + id + "not found");
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

    public List<Category> mainCategoryListByCategoryPage(boolean mainCategory, CategoryPage categoryPage) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(mainCategory, categoryPage);
    }

    public List<Category> mainTranslationPairsCategoryListUser(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByIdAsc(mainCategory, CategoryPage.MINI_STORIES);
    }

    public List<Category> getDtoSubcategoriesInMainCategory(Long id) {
        return categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
    }

    public List<Category> getSubcategoriesPhraseLesson() {
        List<Category> phraseLessonMainCategories = mainCategoryListByCategoryPage(true, CategoryPage.LESSON_PHRASES);
        List<Category> subcategories = new ArrayList<>();
        for (Category arr: phraseLessonMainCategories) {
            subcategories.addAll(arr.getSubcategories());
        }
        return subcategories;
    }

    public List<Category> getSubcategoriesAndSubSubcategoriesInMainCategory(Long id) {
        return categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(id);
    }

    public CustomResponseMessage saveMainCategory(Category category, Category categoryFromDatabase) {
        categoryFromDatabase.setName(category.getName());
        categoryFromDatabase.setDescription(category.getDescription());
        categoryFromDatabase.setMainCategory(true);
        categoryFromDatabase.setViewSubcategoryFullNoInfoOrNameAndInfo(category.isViewSubcategoryFullNoInfoOrNameAndInfo());
//        String page = category.getCategoryPages().get(0).name();
        if (!category.getCategoryPages().equals(CategoryPage.NO_PAGE)) {
            categoryFromDatabase.getCategoryPages().clear();
            categoryFromDatabase.setCategoryPages(category.getCategoryPages());
        }
        if (categoryFromDatabase.getParentCategory() != null) {
            Category parentCategory = categoryFromDatabase.getParentCategory();
            parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(categoryFromDatabase.getId()));
        }
        categoryFromDatabase.setParentCategory(null);
        categoryRepository.save(categoryFromDatabase);
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
        Image image = new Image();
        category.setImage(image);
        categoryRepository.save(category);
        //TODO(Категорія не зміню батьківську, на нову батьківську!!!!)
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }
}
