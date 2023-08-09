package com.example.learnenglish.dto;

import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.TextOfAppPage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
@Getter
@Setter
//@Component
public class DtoTextOfAppPage {

    private TextOfAppPage textOfAppPage;
//        private Long pageApplicationId;

    private PageApplication selectedPageApplication;

    public DtoTextOfAppPage() {
    }

//    public TextOfAppPage getTextOfAppPage() {
//        return textOfAppPage;
//    }
//
//    public void setTextOfAppPage(TextOfAppPage textOfAppPage) {
//        this.textOfAppPage = textOfAppPage;
//    }

//    public Long getPageApplicationId() {
//        return pageApplicationId;
//    }

//    public void setPageApplicationId(Long pageApplicationId) {
//        this.pageApplicationId = pageApplicationId;
//    }


    public PageApplication getSelectedPageApplication() {
        return selectedPageApplication;
    }

    public void setSelectedPageApplication(PageApplication selectedPageApplication) {
        this.selectedPageApplication = selectedPageApplication;
    }
}
