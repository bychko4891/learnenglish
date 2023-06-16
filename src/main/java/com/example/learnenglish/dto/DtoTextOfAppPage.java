package com.example.learnenglish.dto;

import com.example.learnenglish.model.TextOfAppPage;
import org.springframework.stereotype.Component;

@Component
public class DtoTextOfAppPage {

        private TextOfAppPage textOfAppPage;
        private Long pageApplicationId;

    public DtoTextOfAppPage() {
    }

    public TextOfAppPage getTextOfAppPage() {
        return textOfAppPage;
    }

    public void setTextOfAppPage(TextOfAppPage textOfAppPage) {
        this.textOfAppPage = textOfAppPage;
    }

    public Long getPageApplicationId() {
        return pageApplicationId;
    }

    public void setPageApplicationId(Long pageApplicationId) {
        this.pageApplicationId = pageApplicationId;
    }
}
