package com.example.learnenglish.controllers;

import com.example.learnenglish.dto.DtoWordsCategoryToUi;
import com.example.learnenglish.model.*;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

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
    private Principal principal;
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
        List<TextOfAppPage> textOfAppPageList = new ArrayList<>();
        when(textOfAppPageService.getAppTextPageList()).thenReturn(textOfAppPageList);

        var res = adminController.getTextsOfAppPages(model, principal);

        assertEquals("admin/adminTextsOfAppPages", res);
        verify(model).addAttribute("textOfAppPageList", textOfAppPageList);

    }

    @Test
    void getTextsOfAppPagesIfPrincipalNull() {
        principal = null;

        var result = adminController.getTextsOfAppPages(model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(textOfAppPageService);
        verifyNoInteractions(model);
    }

    @Test
    void appInfoListAdminPageIfPrincipalNotNull() {
        var count = 5L;

        when(textOfAppPageService.countTextOfAppPage()).thenReturn(count);

        var result = adminController.appInfoListAdminPage(principal);

        assertEquals("redirect:/admin-page/text-of-app-page/" + (count + 1) + "/new-text-of-app-page-in-editor", result);
        verify(textOfAppPageService).countTextOfAppPage();
    }

    @Test
    void appInfoListAdminPageIfPrincipalNull() {
        principal = null;

        var result = adminController.appInfoListAdminPage(principal);
        assertEquals("redirect:/login", result);
        verifyNoInteractions(textOfAppPageService);
    }

    @Test
    void newTextOfAppPageIfPrincipalNotNull() {
        List<PageApplication> textOfAppPage = new ArrayList<>();
        textOfAppPage.add(new PageApplication(1L, "Page 1"));
        textOfAppPage.add(new PageApplication(2L, "Page 2"));

        when(pageApplicationService.pageApplicationList()).thenReturn(textOfAppPage);

        var result = adminController.newTextOfAppPage(1L, model, principal);

        assertEquals("admin/adminTextOfAppPageInEditor", result);
        verify(model).addAttribute("pageList", textOfAppPage);
        verify(pageApplicationService).pageApplicationList();
    }

    @Test
    void newTextOfAppPageIfPrincipalNull() {
        principal = null;

        var result = adminController.newTextOfAppPage(1L, model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(pageApplicationService);
    }

    @Test
    public void textOfAppPageEditIfPrincipalNotNullAndPageApplicationNotNull() {
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
        verify(model).addAttribute("pageList", pageApplicationList);
    }

    @Test
    public void textOfAppPageEditIfPrincipalNotNullAndPageApplicationNull() {
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
        principal = null;

        var result = adminController.textOfAppPageEdit(1L, null, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(textOfAppPageService);
        verifyNoInteractions(pageApplicationService);
    }

    @Test
    void usersListAdminPageIfPrincipalNotNull() {
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
        principal = null;
        var page = 0;
        var size = 10;

        var result = adminController.usersListAdminPage(model, principal, page, size);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(userService);
    }

    @Test
    void lessonsListAdminPageIfPrincipalNotNull() {
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
        principal = null;
        var message = "Test Message";
        var page = 0;
        var size = 5;

        var result = adminController.lessonsListAdminPage(message, page, size, principal, model);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(lessonService);
    }

    @Test
    public void newLessonAdminPageIfPrincipalNotNull() {
        var redirectAttributes = mock(RedirectAttributes.class);
        var count = 16L;

        when(lessonService.countLessons()).thenReturn(count);

        var result = adminController.newLessonAdminPage(principal, redirectAttributes);

        assertEquals("redirect:/admin-page/lesson/17/new-lesson-in-editor", result);
        verify(lessonService).countLessons();
    }

    @Test
    public void newLessonAdminPageIfPrincipalNotNullAndCountLessonsGreaterThan17() {
        var redirectAttributes = mock(RedirectAttributes.class);
        var count = 18L;

        when(lessonService.countLessons()).thenReturn(count);

        var result = adminController.newLessonAdminPage(principal, redirectAttributes);

        assertEquals("redirect:/admin-page/lessons", result);
        verify(lessonService).countLessons();
        verify(redirectAttributes).addAttribute("message", "Дозволено максимум 17 уроків");
    }

    @Test
    public void newLessonAdminPageIfPrincipalNull() {
        principal = null;
        var redirectAttributes = mock(RedirectAttributes.class);

        var result = adminController.newLessonAdminPage(principal, redirectAttributes);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(lessonService);
        verifyNoInteractions(redirectAttributes);
    }


    @Test
    void newLessonIfPrincipalNotNull() {
        var id = 5L;
        var expectedLesson = new Lesson();
        expectedLesson.setLessonInfo("Lesson description" + id);
        expectedLesson.setName("Test Name");
        expectedLesson.setId(id);

        var result = adminController.newLesson(id, model, principal);

        assertEquals("admin/adminLessonInEditor", result);
        verifyNoInteractions(principal);
    }

    @Test
    void newLessonIfPrincipalNull() {
        principal = null;

        var result = adminController.newLesson(1L, model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);
    }

    @Test
    void lessonEditIfPrincipalNotNull() {
        var id = 2L;
        var expectedLesson = new Lesson();

        when(lessonService.getLesson(id)).thenReturn(expectedLesson);

        var result = adminController.lessonEdit(id, model, principal);

        assertEquals("admin/adminLessonInEditor", result);
        verify(model).addAttribute("lesson", expectedLesson);
        verifyNoInteractions(principal);
    }

    @Test
    void lessonEditIfPrincipalNull() {
        principal = null;

        var id = 1L;

        var result = adminController.lessonEdit(id, model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);
    }

    @Test
    void translationPairsListAdminPageIfPrincipalNotNull() {
        var page = 1;
        var size = 10;

        Page<TranslationPair> translationPairPage = mock(Page.class);

        when(translationPairService.getTranslationPairsFourAdmin(page, size, 1L)).thenReturn(translationPairPage);

        var result = adminController.translationPairsListAdminPage(model, principal, page, size);

        assertEquals("admin/adminTranslationPairs", result);
    }

    @Test
    void translationPairsListAdminPageIfPrincipalNull() {
        principal = null;
        var page = 1;
        var size = 10;

        var result = adminController.translationPairsListAdminPage(model, principal, page, size);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);
    }

    @Test
    void wordsIfPrincipalNotNull() {

        var result = adminController.words(principal);

        assertEquals("admin/wordsMainPage", result);
        verifyNoInteractions(principal);
    }

    @Test
    void wordsIfPrincipalNull() {
        principal = null;

        var result = adminController.words(principal);

        assertEquals("redirect:/login", result);
    }

    @Test
    void wordsCategoryIfPrincipalNotNull() {
        List<DtoWordsCategoryToUi> categoryList = new ArrayList<>();

        when(categoryService.getWordsCategories()).thenReturn(categoryList);

        var result = adminController.wordsCategory(model, principal);

        assertEquals("admin/categories", result);
        verify(categoryService).getWordsCategories();
    }

    @Test
    void wordsCategoryIfPrincipalNull() {
        principal = null;

        var result = adminController.wordsCategory(model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);

    }

    @Test
    void wordsCategoryNewCategoryIfPrincipalNotNull() {
        var count = 3L;

        when(categoryService.countWordCategory()).thenReturn(count);

        var result = adminController.wordsCategoryNewCategory(principal);

        assertEquals("redirect:/admin-page/" + (count + 1) + "/category-edit", result);
    }

    @Test
    void wordsCategoryNewCategoryIfPrincipalNull() {
        principal = null;
        var count = 3L;

        when(categoryService.countWordCategory()).thenReturn(count);

        var result = adminController.wordsCategoryNewCategory(principal);

        assertEquals("redirect:/login", result);
    }

    @Test
    void wordsCategoryNewCategoryIfNullPointerException() {

        when(categoryService.countWordCategory()).thenThrow(NullPointerException.class);

        var result = adminController.wordsCategoryNewCategory(principal);

        assertEquals("redirect:/admin-page/1/category-edit", result);
        verify(categoryService).countWordCategory();
        verifyNoInteractions(principal);

    }

    @Test
    void wordsCategoryEditIfPrincipalNotNull() {
        var id = 3L;
        List<Category> mainWordsCategory = new ArrayList<>();
        var category = new Category();
        category.setId(id);
        category.setName("Category 1");
        category.setMainCategory(true);

        when(categoryService.mainWordCategoryList(true)).thenReturn(mainWordsCategory);
        when(categoryService.getCategoryToEditor(id)).thenReturn(category);

        var result = adminController.wordsCategoryEdit(id, model, principal);

        assertEquals("admin/categoryEdit", result);
        verify(categoryService).mainCategoryList(true);
        verify(categoryService).getCategoryToEditor(id);
        verifyNoInteractions(principal);

    }

    @Test
    void wordsCategoryEditIfPrincipalNull() {
        principal = null;
        var result = adminController.wordsCategoryEdit(1L, model, principal);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);

    }

    @Test
    void wordsListAdminPageIfPrincipalNotNull() {
        var page = 1;
        var size = 10;
        Page<Word> wordPage = mock(Page.class);

        when(wordService.getWordsPage(page, size)).thenReturn(wordPage);

        var result = adminController.wordsListAdminPage(page, size, principal, model);

        assertEquals("admin/words", result);
        verify(wordService).getWordsPage(page, size);
    }

    @Test
    void wordsListAdminPageIfPrincipalNull() {
        principal = null;
        var page = 1;
        var size = 10;

        var result = adminController.wordsListAdminPage(page, size, principal, model);

        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);
    }

    @Test
    void newWordAdminPageIfPrincipalNotNull() {
        var count = 1L;

        var result = adminController.newWordAdminPage(principal);

        assertEquals("redirect:/admin-page/word/" + count + "/new-word-in-editor", result);


    }

    @Test
    void newWordAdminPageIfPrincipalNull() {
        principal = null;

        var result = adminController.newWordAdminPage(principal);

        assertEquals("redirect:/login", result);

    }

    @Test
    void newWordAdminPageIfThrowsNullPointerException() {

        when(wordService.countWords() + 1).thenThrow(NullPointerException.class);

        var result = adminController.newWordAdminPage(principal);

        assertEquals("redirect:/admin-page/word/1/new-word-in-editor", result);
        verify(wordService).countWords();
        verifyNoInteractions(principal);

    }

    @Test
    void testNewWord() {
        var id = 1L;
        List<Category> mainCategoryList = new ArrayList<>();
        when(categoryService.mainWordCategoryList(true)).thenReturn(mainCategoryList);

        var result = adminController.newWord(id, model, principal);

        assertEquals("admin/wordEdit", result);
        verifyNoInteractions(principal);

        principal = null;

        var result1 = adminController.newWord(id, model, principal);

        assertEquals("redirect:/login", result1);
    }

    @Test
    void testWordEdit() {
        var id = 1L;
        Word word = mock(Word.class);
        List<Category> mainCategoryList = new ArrayList<>();
        when(categoryService.mainWordCategoryList(true)).thenReturn(mainCategoryList);
        when(wordService.getWord(id)).thenReturn(word);

        var res = adminController.wordEdit(id, model, principal);

        assertEquals("admin/wordEdit", res);
        verify(model).addAttribute("word", word);
        verify(model).addAttribute("category", "Відсутня");

        principal = null;

        var result = adminController.wordEdit(id, model, principal);

        assertEquals("redirect:/login", result);

    }

    @Test
    void testWordLessonsListAdminPage() {
        var page = 1;
        var size = 10;

        Page<WordLesson> wordLessonPage = mock(Page.class);
        when(wordLessonService.getWordLessonsPage(page, size)).thenReturn(wordLessonPage);

        var res = adminController.wordLessonsListAdminPage(page, size, principal, model);

        assertEquals("admin/wordLessons", res);
        verify(model).addAttribute("totalPages", 1);

        principal = null;

        var result = adminController.wordLessonsListAdminPage(page, size, principal, model);

        assertEquals("redirect:/login", result);

    }

    @Test
    void testNewWordLessonAdminPage() {
        var count = 3L;
        when(wordLessonService.countWordLesson()).thenReturn(count);

        var res = adminController.newWordLessonAdminPage(principal);

        assertEquals("redirect:/admin-page/word-lesson/" + (count + 1) + "/word-lesson-edit", res);

        principal = null;

        var result = adminController.newWordLessonAdminPage(principal);
        assertEquals("redirect:/login", result);

    }

    @Test
    void testNewWordLessonAdminPageNullPointerException() {
        when(wordLessonService.countWordLesson()).thenThrow(NullPointerException.class);

        var resIfNullPointer = adminController.newWordLessonAdminPage(principal);

        assertEquals("redirect:/admin-page/word-lesson/1/word-lesson-edit", resIfNullPointer);
    }


    @Test
    void wordLessonEdit() {
        var id = 1L;
        List<Category> mainCategories = new ArrayList<>();
        WordLesson wordLesson = mock(WordLesson.class);

        when(categoryService.mainWordLessonCategoryList(true)).thenReturn(mainCategories);
        when(wordLessonService.getWordLesson(id)).thenReturn(wordLesson);

        var res = adminController.wordLessonEdit(id, model, principal);

        assertEquals("admin/wordLessonEdit", res);
        verify(model).addAttribute("category", "Відсутня");
        verify(model).addAttribute("wordLesson", wordLesson);
        verify(model).addAttribute("mainCategories", mainCategories);

        principal = null;

        var result = adminController.wordLessonEdit(id, model, principal);

        assertEquals("redirect:/login", result);
    }

    @Test
    void audioListAdminPage() {
        var page = 1;
        var size = 7;
        Page<Audio> wordAudioPage = mock(Page.class);
        when(wordAudioService.getWordsAudioPage(page, size)).thenReturn(wordAudioPage);

        var res = adminController.audioListAdminPage(page, size, principal, model);

        assertEquals("admin/audios", res);
        verify(model).addAttribute("totalPages", 1);

        principal = null;

        var result = adminController.audioListAdminPage(page, size, principal, model);
        assertEquals("redirect:/login", result);

    }

    @Test
    void wordAudioUpload() {
        var id = 1L;

        var wordAudio = new Audio();

        when(wordAudioService.getWordAudio(id)).thenReturn(wordAudio);

        var res = adminController.wordAudioUpload(id, model, principal);

        assertEquals("admin/audioUpload", res);
        verify(model).addAttribute("wordAudio", wordAudio);

        principal = null;

        var result = adminController.wordAudioUpload(id, model, principal);

        assertEquals("redirect:/login", result);

    }

    @Test
    void translationPairsPages() {
        var page = 1;
        var size = 7;
        Page<TranslationPairsPage> translationPairsPages = mock(Page.class);
        when(translationPairPageService.getTranslationPairsPages(page, size)).thenReturn(translationPairsPages);

        var res = adminController.translationPairsPages(page, size, principal, model);

        assertEquals("admin/translationPairPages", res);
        verify(model).addAttribute("totalPages", 1);

        principal = null;

        var result = adminController.translationPairsPages(page, size, principal, model);

        assertEquals("redirect:/login", result);

    }

    @Test
    void newTranslationPairPage() {
        var count = 2L;

        when(translationPairPageService.countTranslationPairPages()).thenReturn(count);

        var res = adminController.newTranslationPairPage(principal);

        assertEquals("redirect:/admin-page/phrase/" + (count + 1) + "/new-page-phrases-create", res);

        principal = null;

        var result = adminController.newTranslationPairPage(principal);

        assertEquals("redirect:/login", result);

    }

    @Test
    void testNewTranslationPairPageCreate() {
        List<Category> mainTranslationPairsPagesCategories = new ArrayList<>();

        when(categoryService.mainTranslationPairsCategoryList(true)).thenReturn(mainTranslationPairsPagesCategories);

        var res = adminController.newTranslationPairPageCreate(1L, model, principal);

        assertEquals("admin/translationPairPageEdit", res);
        verify(model).addAttribute("category", "Відсутня");

        principal = null;

        var result = adminController.newTranslationPairPageCreate(1L, model, principal);

        assertEquals("redirect:/login", result);

    }

    @Test
    void newTranslationPairPageEdit() {
        var id = 1L;
        List<Category> mainTranslationPairsPagesCategories = new ArrayList<>();
        TranslationPairsPage translationPairsPage = mock(TranslationPairsPage.class);
        when(categoryService.mainTranslationPairsCategoryList(true)).thenReturn(mainTranslationPairsPagesCategories);
        when(translationPairPageService.getTranslationPairsPage(id)).thenReturn(translationPairsPage);

        var res = adminController.newTranslationPairPageEdit(id, model, principal);

        assertEquals("admin/translationPairPageEdit", res);
        verify(model).addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
        verify(model).addAttribute("translationPairsPage", translationPairsPage);

        principal = null;

        var result = adminController.newTranslationPairPageEdit(id, model, principal);

        assertEquals("redirect:/login", result);
    }

    @Test
    void imagesPage() {
        var page = 2;
        var size = 7;
        Page<Image> imagesPage = mock(Page.class);

        when(imagesService.getImages(page, size)).thenReturn(imagesPage);

        var res = adminController.imagesPage(page, size, principal, model);

        assertEquals("admin/webImages", res);
        verify(model).addAttribute("images", imagesPage.getContent());
        verify(model).addAttribute("currentPage", page);

        principal = null;

        var result = adminController.imagesPage(page, size, principal, model);

        assertEquals("redirect:/login", result);
    }
}