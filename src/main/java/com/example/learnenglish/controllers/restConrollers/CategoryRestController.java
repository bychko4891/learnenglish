package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.CategoryService;
import com.example.learnenglish.validate.CategoryValidator;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Data
public class CategoryRestController {

    private final CategoryService categoryService;

    private final CategoryValidator categoryValidator;

    @PostMapping("/admin/category-edit/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> saveWordsCategory(@RequestBody Category category,
                                                                   Principal principal) {
        if (principal != null) {
            try {
                Category categoryDb = categoryService.getCategory(category.getId());
                if (category.isMainCategory()) {
                    return ResponseEntity.ok(categoryService.saveMainCategory(category, categoryDb));
                }
                return ResponseEntity.ok(categoryService.saveSubcategory(category, categoryDb));
            } catch (RuntimeException e) {
                return ResponseEntity.ok(categoryService.saveNewCategory(category));
            }


//            Object obj = categoryValidator.categoryIsPresentInBase(category);
//            if (obj instanceof Category categoryFromDatabase) {
//                if (categoryFromDatabase.getId().equals(category.getMainCategoryId()) ||
//                        categoryFromDatabase.getId().equals(category.getSubcategoryId())) {
//                    return ResponseEntity.ok(new CustomResponseMessage(Message.SELF_ASSIGNMENT_CATEGORY_ERROR));
//                } else {
//                    if (category.getMainCategoryId() == 0 && category.getCategory().isMainCategory()) {
//                        Category category = category.getCategory();
//                        category = categoryValidator.categoryPageIsNull(category);
//                        return ResponseEntity.ok(categoryService.saveMainCategory(category, categoryFromDatabase));
//                    } else {
//                        return ResponseEntity.ok(categoryService.saveSubcategory(category, categoryFromDatabase));
//                    }
//                }
//            } else {
//                return ResponseEntity.ok(categoryService.saveNewCategory(category)); //
//            }


        }
        return ResponseEntity.notFound().build();
    }
}
