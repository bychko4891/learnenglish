package com.example.learnenglish.controllers;

import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.PageApplication;
import com.example.learnenglish.model.TextOfAppPage;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest { // Change over. Need refactor

    @Mock
    private HttpSession session;
    @Mock
    private LessonService lessonService;
    @Mock
    private UserService userService;
    @Mock
    private TranslationPairService translationPairService;
    @Mock
    private TextOfAppPageService textOfAppPageService;
    @Mock
    private PageApplicationService pageApplicationService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private WordService wordService;
    @Mock
    private AudioService wordAudioService;
    @Mock
    private TranslationPairPageService translationPairPageService;
    @Mock
    private ImagesService imagesService;
    @Mock
    private WordLessonService wordLessonService;
    @Mock
    private Model model;
    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(session, lessonService, userService, translationPairService,
                textOfAppPageService, pageApplicationService, categoryService, wordService, wordAudioService,
                translationPairPageService, imagesService, wordLessonService);
    }

    @Test
    void adminPageIfPrincipalNotNull() {
        Principal principal = () -> "TestUser";
        var res = adminController.adminPage(principal);
        assertEquals("admin/adminMainPage", res);
    }

    @Test
    void adminPageIfPrincipalNull() {
        var result = adminController.adminPage(null);
        assertEquals("redirect:/login", result);
    }

    @Test
    void getTextsOfAppPagesIfPrincipalNotNull() {
        Principal principal = mock(Principal.class);
        List<TextOfAppPage> textOfAppPageList = new ArrayList<>();
        when(textOfAppPageService.getAppTextPageList()).thenReturn(textOfAppPageList);

        var result = adminController.getTextsOfAppPages(model, principal);

        assertEquals("admin/adminTextsOfAppPages", result);
        verify(model).addAttribute("textOfAppPageList", textOfAppPageList);
    }

    @Test
    void getTextsOfAppPagesIfPrincipalNull() {
        Principal principal = null;

        var result = adminController.getTextsOfAppPages(model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(textOfAppPageService);
        verifyNoInteractions(model);
    }

    @Test
    void appInfoListAdminPageIfPrincipalNotNull() {
        Principal principal = mock(Principal.class);

        when(textOfAppPageService.countTextOfAppPage() + 1).thenReturn(10L);

        var result = adminController.appInfoListAdminPage(principal);

        assertEquals("redirect:/admin-page/text-of-app-page/11/new-text-of-app-page-in-editor", result);
        verify(textOfAppPageService).countTextOfAppPage();
    }

    @Test
    void appInfoListAdminPageIfPrincipalNull() {
        Principal principal = null;

        var result = adminController.appInfoListAdminPage(principal);
        assertEquals("redirect:/login", result);
        verifyNoInteractions(textOfAppPageService);
    }

    @Test
    void newTextOfAppPageIfPrincipalNotNull() {
        Principal principal = mock(Principal.class);
        List<PageApplication> pageApplicationList = new ArrayList<>();
        pageApplicationList.add(new PageApplication(1L, "Page 1"));
        pageApplicationList.add(new PageApplication(2L, "Page 2"));

        when(pageApplicationService.pageApplicationList()).thenReturn(pageApplicationList);

        var result = adminController.newTextOfAppPage(1L, model, principal);

        assertEquals("admin/adminTextOfAppPageInEditor", result);
        verify(pageApplicationService).pageApplicationList();
    }

    @Test
    void newTextOfAppPageIfPrincipalNull() {
        Principal principal = null;

        var result = adminController.newTextOfAppPage(1L, model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(pageApplicationService);
    }

    @Test
    public void textOfAppPageEditIfPrincipalNotNullAndPageApplicationNotNull() {
        Principal principal = mock(Principal.class);
        List<PageApplication> pageApplicationList = new ArrayList<>();
        pageApplicationList.add(new PageApplication(1L, "Page 1"));
        pageApplicationList.add(new PageApplication(2L, "Page 2"));
        TextOfAppPage textOfAppPage = new TextOfAppPage();
        textOfAppPage.setId(1L);
        textOfAppPage.setName("Text Page 1");
        textOfAppPage.setPageApplication(new PageApplication(1L, "Page 1"));

        when(textOfAppPageService.findByIdTextOfAppPage(1L)).thenReturn(textOfAppPage);
        when(pageApplicationService.pageApplicationList()).thenReturn(pageApplicationList);

        String result = adminController.textOfAppPageEdit(1L, model, principal);

        assertEquals("admin/adminTextOfAppPageInEditor", result);
        verify(textOfAppPageService).findByIdTextOfAppPage(1L);
        verify(pageApplicationService).pageApplicationList();
    }

    @Test
    public void textOfAppPageEditIfPrincipalNotNullAndPageApplicationNull() {
        Principal principal = mock(Principal.class);
        List<PageApplication> pageApplicationList = new ArrayList<>();
        pageApplicationList.add(new PageApplication(1L, "Page 1"));
        pageApplicationList.add(new PageApplication(2L, "Page 2"));
        TextOfAppPage textOfAppPage = new TextOfAppPage();
        textOfAppPage.setId(1L);
        textOfAppPage.setName("Text Page 1");

        when(textOfAppPageService.findByIdTextOfAppPage(1L)).thenReturn(textOfAppPage);
        when(pageApplicationService.pageApplicationList()).thenReturn(pageApplicationList);

        var result = adminController.textOfAppPageEdit(1L, model, principal);

        assertEquals("admin/adminTextOfAppPageInEditor", result);
        verify(textOfAppPageService).findByIdTextOfAppPage(1L);
        verify(pageApplicationService).pageApplicationList();
    }

    @Test
    public void textOfAppPageEditIfPrincipalNull() {
        Principal principal = null;

        // Act
        String result = adminController.textOfAppPageEdit(1L, null, principal);

        // Assert
        assertEquals("redirect:/login", result);
        verifyNoInteractions(textOfAppPageService);
        verifyNoInteractions(pageApplicationService);
    }

    @Test
    void usersListAdminPageIfPrincipalNotNull() {
        Principal principal = mock(Principal.class);
        var page = 0;
        var size = 10;
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            userList.add(new User());
        }
        Page<User> userPage = new PageImpl<>(userList);

        when(userService.getUsersPage(page, size)).thenReturn(userPage);

        var result = adminController.usersListAdminPage(model, principal, page, size);

        assertEquals("admin/adminUsers", result);
        verify(userService).getUsersPage(page, size);
    }

    @Test
    void usersListAdminPageIfPrincipalNull() {
        Principal principal = null;
        var page = 0;
        var size = 10;

        var result = adminController.usersListAdminPage(model, principal, page, size);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(userService);
    }

    @Test
    void lessonsListAdminPageIfPrincipalNotNull() {
        Principal principal = mock(Principal.class);
        var message = "Test Message";
        var page = 0;
        var size = 5;

        List<Lesson> lessonList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            lessonList.add(new Lesson());
        }
        Page<Lesson> lessonPage = new PageImpl<>(lessonList);

        when(lessonService.getLessonsPage(page, size)).thenReturn(lessonPage);

        var result = adminController.lessonsListAdminPage(message, page, size, principal, model);

        assertEquals("admin/adminLessons", result);
        verify(lessonService).getLessonsPage(page, size);
    }

    @Test
    void lessonsListAdminPageIfPrincipalNull() {
        Principal principal = null;
        var message = "Test Message";
        var page = 0;
        var size = 5;

        var result = adminController.lessonsListAdminPage(message, page, size, principal, model);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(lessonService);
    }

    @Test
    @Disabled
    void newLessonAdminPage() {
    }

    @Test
    @Disabled
    void newLesson() {
    }

    @Test
    @Disabled
    void lessonEdit() {
    }

    @Test
    @Disabled
    void translationPairsListAdminPage() {
    }

    @Test
    @Disabled
    void words() {
    }

    @Test
    @Disabled
    void wordsCategory() {
    }

    @Test
    @Disabled
    void wordsCategoryNewCategory() {
    }

    @Test
    @Disabled
    void wordsCategoryEdit() {
    }

    @Test
    @Disabled
    void wordsListAdminPage() {
    }

    @Test
    @Disabled
    void newWordAdminPage() {
    }

    @Test
    @Disabled
    void newWord() {
    }

    @Test
    @Disabled
    void wordEdit() {
    }

    @Test
    @Disabled
    void wordLessonsListAdminPage() {
    }

    @Test
    @Disabled
    void newWordLessonAdminPage() {
    }

    @Test
    @Disabled
    void wordLessonEdit() {
    }

    @Test
    @Disabled
    void audioListAdminPage() {
    }

    @Test
    @Disabled
    void wordAudioUpload() {
    }

    @Test
    @Disabled
    void translationPairsPages() {
    }

    @Test
    @Disabled
    void newTranslationPairPage() {
    }

    @Test
    @Disabled
    void newTranslationPairPageCreate() {
    }

    @Test
    @Disabled
    void newTranslationPairPageEdit() {
    }

    @Test
    @Disabled
    void imagesPage() {
    }
}