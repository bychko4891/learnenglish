package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.repository.TextOfAppPageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TextOfAppPageServiceTest {

    @Mock
    private TextOfAppPageRepository textOfAppPageRepository;

    @InjectMocks
    private TextOfAppPageService textOfAppPageService;
    @Mock
    private PageApplicationService pageApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        textOfAppPageService = new TextOfAppPageService(textOfAppPageRepository, pageApplicationService);
    }

    @Test
    public void testGetAppTextPageList() {
        List<TextOfAppPage> expectedTextOfAppPageList = Collections.emptyList();
        when(textOfAppPageRepository.findAll()).thenReturn(expectedTextOfAppPageList);

        List<TextOfAppPage> result = textOfAppPageService.getAppTextPageList();

        assertEquals(expectedTextOfAppPageList, result);
    }

    @Test
    public void testCountTextOfAppPage() {
        Long expectedCount = 10L; // Наприклад, 10 записів у базі даних
        when(textOfAppPageRepository.count()).thenReturn(expectedCount);

        Long result = textOfAppPageService.countTextOfAppPage();

        assertEquals(expectedCount, result);
    }

    @Test
    public void testFindByIdTextOfAppPageWhenTextOfAppPageExists() {
        Long id = 1L;
        TextOfAppPage expectedTextOfAppPage = new TextOfAppPage();
        when(textOfAppPageRepository.findById(id)).thenReturn(Optional.of(expectedTextOfAppPage));

        TextOfAppPage result = textOfAppPageService.findByIdTextOfAppPage(id);

        assertEquals(expectedTextOfAppPage, result);
    }

    @Test
    public void testFindByIdTextOfAppPageWhenTextOfAppPageDoesNotExist() {
        Long id = 1L;

        when(textOfAppPageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            textOfAppPageService.findByIdTextOfAppPage(id);
        });
    }
}
