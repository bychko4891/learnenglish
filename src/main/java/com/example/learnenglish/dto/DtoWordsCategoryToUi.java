package com.example.learnenglish.dto;

import com.example.learnenglish.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class DtoWordsCategoryToUi {
    private Long id;
    private String name;
    private boolean mainCategory;
    private String info;
    private Category parentCategory;
    private List<DtoWordsCategoryToUi> subcategories;

    public DtoWordsCategoryToUi() {
    }

    public static DtoWordsCategoryToUi fromEntity(Category wordCategory) {
        DtoWordsCategoryToUi dto = new DtoWordsCategoryToUi();
        dto.setId(wordCategory.getId());
        dto.setName(wordCategory.getName());
        dto.setMainCategory(wordCategory.isMainCategory());
        dto.setInfo(wordCategory.getInfo());
        dto.setParentCategory(wordCategory.getParentCategory());

        List<Category> subcategories = wordCategory.getSubcategories();
        if (subcategories != null && !subcategories.isEmpty()) {
            List<DtoWordsCategoryToUi> subcategoryDTOs = new ArrayList<>();
            for (Category subcategory : subcategories) {
                DtoWordsCategoryToUi subcategoryDTO = DtoWordsCategoryToUi.fromEntity(subcategory);
                subcategoryDTOs.add(subcategoryDTO);
            }
            dto.setSubcategories(subcategoryDTOs);
        }
        return dto;
    }
    public static DtoWordsCategoryToUi subcategoriesEditorConvertToDto(Category wordCategory) {
        DtoWordsCategoryToUi dto = new DtoWordsCategoryToUi();
        dto.setId(wordCategory.getId());
        dto.setName(wordCategory.getName());
        dto.setInfo(wordCategory.getInfo());
        return dto;
    }

}
