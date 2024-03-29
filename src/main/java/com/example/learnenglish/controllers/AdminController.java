package com.example.learnenglish.controllers;
/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.*;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final HttpSession session;
    private final LessonService lessonService;
    private final UserService userService;
    private final TranslationPairService translationPairService;
    private final TextOfAppPageService textOfAppPageService;
    private final PageApplicationService pageApplicationService;
    private final CategoryService categoryService;
    private final WordService wordService;
    private final AudioService wordAudioService;
    private final TranslationPairPageService translationPairPageService;
    private final ImagesService imagesService;
    private final WordLessonService wordLessonService;
    private final WayForPayModuleService wayForPayModuleService;



    @GetMapping
    public String adminPage(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("wayForPaySettings", wayForPayModuleService.getWayForPayModule());

            return "admin/mainAdmin";
        }
        return "redirect:/login";
    }

    @GetMapping("/texts-of-app-pages")
    public String getTextsOfAppPages(Model model, Principal principal) {
        if (principal != null) {
            List<TextOfAppPage> textOfAppPageList = textOfAppPageService.getAppTextPageList();
            model.addAttribute("textOfAppPageList", textOfAppPageList);
            return "admin/adminTextsOfAppPages";
        }
        return "redirect:/login";
    }

    @GetMapping("/texts-of-app-pages/new-app-text-page")
    public String appInfoListAdminPage(Principal principal) {
        if (principal != null) {
            Long count = textOfAppPageService.countTextOfAppPage() + 1;
            return "redirect:/admin-page/text-of-app-page/" + count + "/new-text-of-app-page-in-editor";
        }
        return "redirect:/login";
    }

    @GetMapping("/text-of-app-page/{id}/new-text-of-app-page-in-editor")
    public String newTextOfAppPage(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            List<PageApplication> pageApplicationList = pageApplicationService.pageApplicationList();
//            lesson = lessonService.findById(id);
            TextOfAppPage textOfAppPage = new TextOfAppPage();
            textOfAppPage.setId(id);
            textOfAppPage.setName("Enter name");
            textOfAppPage.setText("Enter text");
            textOfAppPage.setPageApplication(new PageApplication(0l, "Сторінка відсутня"));
            model.addAttribute("textOfAppPage", textOfAppPage);
            model.addAttribute("pageList", pageApplicationList);
            return "admin/adminTextOfAppPageInEditor";
        }
        return "redirect:/login";
    }

    @GetMapping("/text-of-app-page/{id}/text-of-app-page-in-editor")
    public String textOfAppPageEdit(@PathVariable("id") Long id,
                                    Model model,
                                    Principal principal) {
        if (principal != null) {
            TextOfAppPage textOfAppPage = textOfAppPageService.findByIdTextOfAppPage(id);
            List<PageApplication> pageApplicationList = pageApplicationService.pageApplicationList();
            if (textOfAppPage.getPageApplication() != null) {
                model.addAttribute("pageName",textOfAppPage.getPageApplication().getNamePage());
            } else {
                model.addAttribute("pageName", "Сторінка відсутня");
            }
            model.addAttribute("textOfAppPage", textOfAppPage);
            model.addAttribute("pageList", pageApplicationList);
            return "admin/adminTextOfAppPageInEditor";
        }
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String usersListAdminPage(Model model,
                                     Principal principal,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (principal != null) {
            Page<User> userPage = userService.getUsersPage(page, size);
            model.addAttribute("users", userPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userPage.getTotalPages());
            return "admin/adminUsers";
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons")
    public String lessonsListAdminPage(@RequestParam(value = "message", required = false) String message,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "8", required = false) int size,
                                       Principal principal,
                                       Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Lesson> lessonPage = lessonService.getLessonsPage(page, size);
            if (lessonPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", lessonPage.getTotalPages());
            }
            model.addAttribute("message", message);
            model.addAttribute("lessons", lessonPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/adminLessons";
        }
        return "redirect:/login";
    }

    @GetMapping("/lessons/new-lesson")
    public String newLessonAdminPage(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal != null) {
            Long count = lessonService.countLessons() + 1;
            if (count > 17) {
                String message = "Дозволено максимум 17 уроків";
                redirectAttributes.addAttribute("message", message);
                return "redirect:/admin-page/lessons";
            }
            return "redirect:/admin-page/lesson/" + count + "/new-lesson-in-editor";
        }
        return "redirect:/login";
    }

    @GetMapping("/lesson/{id}/new-lesson-in-editor")
    public String newLesson(@PathVariable("id") Long id,
                            Model model,
                            Principal principal) {
        if (principal != null) {
//            lesson = lessonService.findById(id);
            Lesson lesson = new Lesson();
            lesson.setId(id);
            lesson.setName("Заняття № " + id);
            lesson.setLessonInfo("Опис заняття");
            model.addAttribute("lesson", lesson);
            return "admin/adminLessonInEditor";
        }
        return "redirect:/login";
    }

    @GetMapping("/lesson/{id}/lesson-in-editor")
    public String lessonEdit(@PathVariable("id") Long id,
                             Model model,
                             Principal principal) {
        if (principal != null) {
            Lesson lesson = lessonService.getLesson(id);
            model.addAttribute("lesson", lesson);
            return "admin/adminLessonInEditor";
        }
        return "redirect:/login";
    }

    @GetMapping("/translation-pairs")
    public String translationPairsListAdminPage(Model model,
                                                Principal principal,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (principal != null) {
            Page<TranslationPair> translationPairsPage = translationPairService.getTranslationPairsFourAdmin(page, size, 1l);
            model.addAttribute("translationPairs", translationPairsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", translationPairsPage.getTotalPages());

            return "admin/adminTranslationPairs";
        }
        return "redirect:/login";
    }

    @GetMapping("/words-main-page")
    public String words(Principal principal) {
        if (principal != null) {
            return "admin/wordsMainPage";
        } else return "redirect:/login";
    }

    @GetMapping("/categories")
    public String wordsCategory(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("wordCategories", categoryService.getWordsCategories());
            return "admin/categories";
        }
        return "redirect:/login";
    }

    @GetMapping("/categories/new-category")
    public String wordsCategoryNewCategory(Principal principal) {
        if (principal != null) {
            Long count = 0l;
            try {
                count = categoryService.countCategory() + 1;
            } catch(NullPointerException e){
                return "redirect:/admin-page/1/category-edit";
            }
            return "redirect:/admin-page/" + count + "/category-edit";
        }
        return "redirect:/login";
    }


    @GetMapping("/{id}/category-edit")
    public String wordsCategoryEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainWordsCategories = categoryService.mainCategoryList(true);
            Category category = categoryService.getCategoryToEditor(id);
            if(category.isMainCategory()){
                mainWordsCategories.removeIf(obj -> obj.getId().equals(id));
            }
            model.addAttribute("parentCategory", "Відсутня");
            if (category.getParentCategory() != null) {
                model.addAttribute("parentCategory", category.getParentCategory().getName());
            }
            model.addAttribute("category", category);
            model.addAttribute("mainCategories", mainWordsCategories);
            return "admin/categoryEdit";
        }
        return "redirect:/login";
    }

    @GetMapping("/words")
    public String wordsListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "30", required = false) int size,
                                     Principal principal,
                                     Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Word> wordPage = wordService.getWordsPage(page, size);
            if (wordPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", wordPage.getTotalPages());
            }
            model.addAttribute("words", wordPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/words";
        }
        return "redirect:/login";
    }

    @GetMapping("/words/new-word")
    public String newWordAdminPage(Principal principal) {
        if (principal != null) {
            try {
                Long count = wordService.countWords() + 1;
                return "redirect:/admin-page/words/" + count + "/word-edit";
            }catch (NullPointerException e){
                return "redirect:/admin-page/words/1/word-edit";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/words/{id}/word-edit")
    public String wordEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainWordsCategories = categoryService.mainWordCategoryList(true);
            Word word = wordService.getWord(id);
            model.addAttribute("category", "Відсутня");
            if (word.getWordCategory() != null) {
                model.addAttribute("category", word.getWordCategory().getName());
            }
            model.addAttribute("word", word);
            model.addAttribute("mainWordsCategories", mainWordsCategories);
            return "admin/wordEdit";
        }
        return "redirect:/login";
    }

    @GetMapping("/word-lessons")
    public String wordLessonsListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                     Principal principal,
                                     Model model) {

        if (principal != null) {
            if (page < 0) page = 0;
            Page<WordLesson> wordLessonsPage = wordLessonService.getWordLessonsPage(page, size);
            if (wordLessonsPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", wordLessonsPage.getTotalPages());
            }
            model.addAttribute("wordLessons", wordLessonsPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/wordLessons";
        }
        return "redirect:/login";
    }
    @GetMapping("/word-lessons/new-lesson")
    public String newWordLessonAdminPage(Principal principal) {
        if (principal != null) {
            try {
                Long count = wordLessonService.countWordLesson() + 1;
                return "redirect:/admin-page/word-lesson/" + count + "/word-lesson-edit";
            }catch (NullPointerException e){
                return "redirect:/admin-page/word-lesson/1/word-lesson-edit";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/word-lesson/{id}/word-lesson-edit")
    public String wordLessonEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.mainWordLessonCategoryList(true);
            WordLesson wordLesson = wordLessonService.getWordLesson(id);
            model.addAttribute("category", "Відсутня");
            if (wordLesson.getCategory() != null) {
                model.addAttribute("category", wordLesson.getCategory().getName());
            }
            model.addAttribute("wordLesson", wordLesson);
            model.addAttribute("mainCategories", mainCategories);
            return "admin/wordLessonEdit";
        }
        return "redirect:/login";
    }


    @GetMapping("/audios")
    public String audioListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "30", required = false) int size,
                                     Principal principal,
                                     Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Audio> wordAudioPage = wordAudioService.getAudioPage(page, size);
            if (wordAudioPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", wordAudioPage.getTotalPages());
            }
            model.addAttribute("audios", wordAudioPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/audios";
        }
        return "redirect:/login";
    }

    @GetMapping("/word-audio/{id}/audio-upload-page")
    public String wordAudioUpload(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            Audio wordAudio = wordAudioService.getAudio(id);
            model.addAttribute("wordAudio", wordAudio);
            return "admin/audioUpload";
        }
        return "redirect:/login";
    }

    @GetMapping("/phrases-pages")
    public String translationPairsPages(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                        Principal principal,
                                        Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<TranslationPairsPage> translationPairsPages = translationPairPageService.getTranslationPairsPages(page, size);
            if (translationPairsPages.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", translationPairsPages.getTotalPages());
            }
            model.addAttribute("translationPairsPages", translationPairsPages.getContent());
            model.addAttribute("currentPage", page);

            return "admin/translationPairPages";
        }
        return "redirect:/login";
    }

    @GetMapping("/phrases-pages/new-page-phrases")
    public String newTranslationPairPage(Principal principal) {
        if (principal != null) {
            Long count = translationPairPageService.countTranslationPairPages() + 1;
            return "redirect:/admin-page/phrase/" + count + "/new-page-phrases-create";
        }
        return "redirect:/login";
    }

    @GetMapping("/phrase/{id}/new-page-phrases-create")
    public String newTranslationPairPageCreate(@PathVariable("id") Long id,
                                               Model model,
                                               Principal principal) {
        if (principal != null) {
            List<Category> mainTranslationPairsPagesCategories = categoryService.mainTranslationPairsCategoryList(true);
            if (mainTranslationPairsPagesCategories != null) {
                model.addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
            }
            TranslationPairsPage translationPairsPage = new TranslationPairsPage();
            translationPairsPage.setId(id);
            translationPairsPage.setName("Enter name");
            translationPairsPage.setInfo("Enter text");
            model.addAttribute("translationPairsPage", translationPairsPage);
            model.addAttribute("category", "Відсутня");

            return "admin/translationPairPageEdit";
        }
        return "redirect:/login";
    }

    @GetMapping("/phrase/{id}/page-phrases-edit")
    public String newTranslationPairPageEdit(@PathVariable("id") Long id,
                                             Model model,
                                             Principal principal) {
        if (principal != null) {
            List<Category> mainTranslationPairsPagesCategories = categoryService.mainTranslationPairsCategoryList(true);
            TranslationPairsPage translationPairsPage = translationPairPageService.getTranslationPairsPage(id);
            model.addAttribute("category", "Відсутня");
            if(translationPairsPage.getTranslationPairsPageCategory() != null){
                model.addAttribute("category", translationPairsPage.getTranslationPairsPageCategory().getName());
            }

            if (mainTranslationPairsPagesCategories != null) {
                model.addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
            }
            model.addAttribute("translationPairsPage", translationPairsPage);


            return "admin/translationPairPageEdit";
        }
        return "redirect:/login";
    }


    @GetMapping("/images")
    public String imagesPage(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                             Principal principal,
                             Model model) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Image> imagesPage = imagesService.getImages(page, size);
            if (imagesPage.getTotalPages() == 0) {
                model.addAttribute("totalPages", 1);
            } else {
                model.addAttribute("totalPages", imagesPage.getTotalPages());
            }
            model.addAttribute("images", imagesPage.getContent());
            model.addAttribute("currentPage", page);

            return "admin/webImages";
        }
        return "redirect:/login";
    }

}