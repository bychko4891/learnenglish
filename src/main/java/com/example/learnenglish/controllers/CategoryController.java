package com.example.learnenglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.Category;
import com.example.learnenglish.service.CategoryService;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@Data
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/admin/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String allCategories(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/categories";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/categories/new-category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordsCategoryNewCategory(Principal principal) {
        if (principal != null) {
            try {
                long count = categoryService.countCategory() + 1;
                return "redirect:/admin/category-edit/" + count;
            } catch (NullPointerException e) {
                return "redirect:/admin/category-edit/1";
            }
        }
        return "redirect:/login";
    }


    @GetMapping("/admin/category-edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String wordsCategoryEdit(@PathVariable("id") long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.mainCategoryList(true);
            try {
                Category category = categoryService.getCategory(id);
                if (category.isMainCategory()) {
                    mainCategories.removeIf(obj -> obj.getId().equals(id));
                }
                model.addAttribute("parentCategory", "Відсутня");
                if (category.getParentCategory() != null) {
                    model.addAttribute("parentCategory", category.getParentCategory().getName());
                }
                model.addAttribute("category", category);
            } catch (RuntimeException e) {
                Category category = categoryService.getNewCategory(id);
                model.addAttribute("category", category);
            }
            model.addAttribute("mainCategories", mainCategories);

            return "admin/categoryEdit";
        }
        return "redirect:/login";
    }

}
