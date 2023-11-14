package com.example.learnenglish.dto;

import com.example.learnenglish.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DtoCategoryFromEditor {

    private Category category;

    private Long mainCategoryId;

    private Long subcategoryId;

}
