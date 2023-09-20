package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.CategoryPage;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.responsemessage.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getCategoryToEditor() {
        var categoryId = 1L;
        var category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");
        category.setInfo("Test Info");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        var result = categoryService.getCategoryToEditor(categoryId);

        assertEquals(categoryId, result.getId());
        assertEquals("Test Category", result.getName());
        assertEquals("Test Info", result.getInfo());

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void testGetCategoryToEditorNonExistingCategory() {
        var categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        var result = categoryService.getCategoryToEditor(categoryId);

        assertEquals(categoryId, result.getId());
        assertEquals("Enter new name category", result.getName());
        assertEquals("Enter new info category", result.getInfo());

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }


    @Test
    void getWordsCategories() {
        List categories = new ArrayList<>();
        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setInfo("Info 1");
        categories.add(category1);

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setInfo("Info 2");
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        var result = categoryService.getWordsCategories();

        assertEquals(2, result.size());

        var dtoCategory1 = result.get(0);
        assertEquals(1L, dtoCategory1.getId());
        assertEquals("Category 1", dtoCategory1.getName());
        assertEquals("Info 1", dtoCategory1.getInfo());

        var dtoCategory2 = result.get(1);
        assertEquals(2L, dtoCategory2.getId());
        assertEquals("Category 2", dtoCategory2.getName());
        assertEquals("Info 2", dtoCategory2.getInfo());

        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void countWordCategory() {
        when(categoryRepository.lastId()).thenReturn(10L);

        var res = categoryService.countWordCategory();

        assertEquals(10L, res);

        verify(categoryRepository, times(1)).lastId();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void mainCategoryList() {
        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        when(categoryRepository.findCategoriesByMainCategoryOrderByNameAsc(true))
                .thenReturn(Arrays.asList(category1, category2));

        var result = categoryService.mainCategoryList(true);

        assertEquals(2, result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByMainCategoryOrderByNameAsc(true);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void mainWordCategoryList() {
        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        when(categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(true, CategoryPage.WORDS))
                .thenReturn(Arrays.asList(category1, category2));

        var result = categoryService.mainWordCategoryList(true);

        assertEquals(2, result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(true, CategoryPage.WORDS);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void mainWordLessonCategoryList() {
        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        when(categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(true, CategoryPage.LESSON_WORDS))
                .thenReturn(Arrays.asList(category1, category2));

        List<Category> result = categoryService.mainWordLessonCategoryList(true);

        assertEquals(2, result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(true, CategoryPage.LESSON_WORDS);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void mainTranslationPairsCategoryList() {
        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        when(categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(true, CategoryPage.TRANSLATION_PAIRS))
                .thenReturn(Arrays.asList(category1, category2));

        var result = categoryService.mainTranslationPairsCategoryList(true);

        assertEquals(2, result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByMainCategoryAndCategoryPagesOrderByNameAsc(true, CategoryPage.TRANSLATION_PAIRS);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void mainTranslationPairsCategoryListUser() {
        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        when(categoryRepository.findCategoriesByMainCategoryAndCategoryPagesOrderByIdAsc(true, CategoryPage.TRANSLATION_PAIRS))
                .thenReturn(Arrays.asList(category1, category2));

        var result = categoryService.mainTranslationPairsCategoryListUser(true);

        assertEquals(2, result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByMainCategoryAndCategoryPagesOrderByIdAsc(true, CategoryPage.TRANSLATION_PAIRS);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getDtoSubcategoriesInMainCategory() {
        var subcategory1 = new Category();
        subcategory1.setId(1L);
        subcategory1.setName("Subcategory 1");

        var subcategory2 = new Category();
        subcategory2.setId(2L);
        subcategory2.setName("Subcategory 2");

        when(categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(1L))
                .thenReturn(Arrays.asList(subcategory1, subcategory2));

        var result = categoryService.getDtoSubcategoriesInMainCategory(1L);

        assertEquals(2, result.size());
        assertEquals(subcategory1.getId(), result.get(0).getId());
        assertEquals(subcategory1.getName(), result.get(0).getName());
        assertEquals(subcategory2.getId(), result.get(1).getId());
        assertEquals(subcategory2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByParentCategory_IdOrderByNameAsc(1L);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getSubcategoriesAndSubSubcategoriesInMainCategory() {
        var subcategory1 = new Category();
        subcategory1.setId(1L);
        subcategory1.setName("Subcategory 1");

        var subcategory2 = new Category();
        subcategory2.setId(2L);
        subcategory2.setName("Subcategory 2");

        when(categoryRepository.findCategoriesByParentCategory_IdOrderByNameAsc(1L))
                .thenReturn(Arrays.asList(subcategory1, subcategory2));

        var result = categoryService.getSubcategoriesAndSubSubcategoriesInMainCategory(1L);

        assertEquals(2, result.size());
        assertEquals(subcategory1.getId(), result.get(0).getId());
        assertEquals(subcategory1.getName(), result.get(0).getName());
        assertEquals(subcategory2.getId(), result.get(1).getId());
        assertEquals(subcategory2.getName(), result.get(1).getName());

        verify(categoryRepository, times(1)).findCategoriesByParentCategory_IdOrderByNameAsc(1L);
        verifyNoMoreInteractions(categoryRepository);
    }

}