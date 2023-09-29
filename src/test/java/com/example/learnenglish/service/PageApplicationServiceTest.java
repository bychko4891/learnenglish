package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.repository.PageApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PageApplicationServiceTest {

    @Mock
    private PageApplicationRepository pageApplicationRepository;
    @InjectMocks
    private PageApplicationService pageApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pageApplicationService = new PageApplicationService(pageApplicationRepository);
    }

    @Test
    void savePageApplication() {
        var pageApplication = new PageApplication();
        pageApplicationService.savePageApplication(pageApplication);

        verify(pageApplicationRepository).save(pageApplication);
    }

    @Test
    void getPageApplication() {
        var id = 1L;
        var expectedPage = new PageApplication();
        expectedPage.setId(id);

        when(pageApplicationRepository.findById(id)).thenReturn(Optional.of(expectedPage));

        var actualPage = pageApplicationService.getPageApplication(id);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getPageApplicationThrowRuntimeException() {
        var id = 1L;

        assertThrows(RuntimeException.class, () -> pageApplicationService.getPageApplication(id));

    }

    @Test
    void pageApplicationList() {
        List<PageApplication> expectedPageApplicationList = new ArrayList<>();
        var pageApplication = new PageApplication();
        var pageApplication1 = new PageApplication();

        expectedPageApplicationList.add(pageApplication);
        expectedPageApplicationList.add(pageApplication1);

        when(pageApplicationRepository.findAll()).thenReturn(expectedPageApplicationList);

        List<PageApplication> actualPageApplicationList = pageApplicationService.pageApplicationList();

        assertEquals(expectedPageApplicationList, actualPageApplicationList);


    }
}