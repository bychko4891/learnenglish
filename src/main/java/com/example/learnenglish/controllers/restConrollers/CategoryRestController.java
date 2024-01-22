package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.Image;
import com.example.learnenglish.model.VocabularyPage;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.service.CategoryService;
import com.example.learnenglish.service.FileStorageService;
import com.example.learnenglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@Data
public class CategoryRestController {

    @Value("${file.upload-category-image}")
    private String categoryStorePath;

    private final CategoryService categoryService;

    private final FileStorageService fileStorageService;


    @PostMapping("/admin/category-edit/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> savesCategory(@RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                               @RequestPart(value = "category") Category category,
                                                               Principal principal) throws IOException {
        if (principal != null) {
            if (category.getId().equals(category.getParentCategory().getId()))
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR));
            try {
                Category categoryDb = categoryService.getCategory(category.getId());
                category.setImage(new Image());
                if (imageFile != null) {
                    category.getImage().setImageName(fileStorageService.storeFile(imageFile, categoryStorePath, category.getName()));
                    if (categoryDb.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(categoryDb.getImage().getImageName(), categoryStorePath);
                }
                if (category.isMainCategory() && category.getParentCategory().getId() == 0) {
                    return ResponseEntity.ok(categoryService.saveMainCategory(category, categoryDb));
                }
                return ResponseEntity.ok(categoryService.saveSubcategory(category, categoryDb));
            } catch (ObjectNotFoundException e) {
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, categoryStorePath, category.getName()));
                category.setImage(image);
                return ResponseEntity.ok(categoryService.saveNewCategory(category));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/getSubcategories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewAllCategories.class)
    public ResponseEntity<List<Category>> wordsSubcategories(@RequestParam("mainCategoryId") Long id, Principal principal) {
        if (principal != null && id != 0) {
            return ResponseEntity.ok(categoryService.getDtoSubcategoriesInMainCategory(id));
        }
        return ResponseEntity.notFound().build();
    }
}
