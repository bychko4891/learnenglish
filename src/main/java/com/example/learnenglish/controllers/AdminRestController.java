package com.example.learnenglish.controllers;
/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.*;
import com.example.learnenglish.exception.FileFormatException;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.WayForPayModule;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.service.*;
import com.example.learnenglish.validate.CategoryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminRestController {
    private final LessonService lessonService;
    private final UserService userService;
    private final TextOfAppPageService textOfAppPageService;
    private final CategoryService categoryService;
    private final WordService wordService;
    private final AudioService audioService;
    private final TranslationPairService translationPairService;
    private final ImagesService imagesService;
    private final TranslationPairPageService translationPairPageService;
    private final WordLessonService wordLessonService;
    private final CategoryValidator categoryValidator;
    private final WayForPayModuleService wayForPayModuleService;


    @PostMapping("/text-of-app-page/{id}/edit")
    public ResponseEntity<CustomResponseMessage> createAppTextPage(@RequestBody DtoTextOfAppPage dtoTextOfAppPage,
                                                                   Principal principal) {
        if (principal != null) {
//            textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage);
            return ResponseEntity.ok(textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/lesson-save")
    public ResponseEntity<CustomResponseMessage> lessonsSave(@RequestBody Lesson lesson,
                                                             Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(lessonService.saveLesson(lesson));
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/users/user/user-active")
    @ResponseBody
    public void userFieldActive(@RequestParam("userActive") boolean userActive,
                                @RequestParam("userId") Long userId,
                                Principal principal) {
        if (principal != null) {
            userService.userActiveEditAdminPage(userId, userActive);
            System.out.println(userActive);
//                return ResponseEntity.ok(new ResponseStatus(Message.SUCCESS_CREATELESSON));
        }
//        return ResponseEntity.ok(new ResponseStatus(Message.ERROR_CREATELESSON));
    }

    @GetMapping("/getSubcategories")
    public ResponseEntity<List<DtoWordsCategoryToUi>> wordsSubcategories(@RequestParam("mainCategoryId") Long id, Principal principal) {
        if (principal != null && id != 0) {
            return ResponseEntity.ok(categoryService.getDtoSubcategoriesInMainCategory(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/category-save")
    public ResponseEntity<CustomResponseMessage> SaveWordsCategory(@RequestBody DtoCategoryFromEditor categoryRequest,
                                                                   Principal principal) {
        if (principal != null && categoryRequest.getMainCategoryId() != null && categoryRequest.getSubcategoryId() != null) {
            Object obj = categoryValidator.categoryIsPresentInBase(categoryRequest);
            if (obj instanceof Category categoryFromDatabase) {
                if (categoryFromDatabase.getId().equals(categoryRequest.getMainCategoryId()) ||
                        categoryFromDatabase.getId().equals(categoryRequest.getSubcategoryId())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.SELF_ASSIGNMENT_CATEGORY_ERROR));
                } else {
                    if (categoryRequest.getMainCategoryId() == 0 && categoryRequest.getCategory().isMainCategory()) {
                        Category category = categoryRequest.getCategory();
                        category = categoryValidator.categoryPageIsNull(category);
                        return ResponseEntity.ok(categoryService.saveMainCategory(category, categoryFromDatabase));
                    } else {
                        return ResponseEntity.ok(categoryService.saveSubcategory(categoryRequest, categoryFromDatabase));
                    }
                }
            } else {
                return ResponseEntity.ok(categoryService.saveNewCategory(categoryRequest)); //
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word-save")
    public ResponseEntity<CustomResponseMessage> uploadAudioFiles(@RequestBody DtoWord dtoWord,
                                                                  Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(wordService.saveWord(dtoWord));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word-lesson-save")
    public ResponseEntity<CustomResponseMessage> saveWordLesson(@RequestBody DtoWordLesson dtoWordLesson,
                                                                Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(wordLessonService.saveWordLesson(dtoWordLesson));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word-audio/{id}/upload")
    public ResponseEntity<CustomResponseMessage> uploadAudioFiles(@PathVariable("id") Long audioId,
                                                                  @RequestParam("brAudio") MultipartFile brAudio,
                                                                  @RequestParam("usaAudio") MultipartFile usaAudio,
                                                                  Principal principal) {
        if (principal != null) {
            Audio audio = audioService.getAudio(audioId);
            if(audio.getBrAudioName() != null)audioService.deleteAudioFilesFromDirectory(audio.getBrAudioName());
            if(audio.getUsaAudioName() != null)audioService.deleteAudioFilesFromDirectory(audio.getUsaAudioName());
            Map<String, MultipartFile> audioFiles = new HashMap<>();
            audioFiles.put("brAudio", brAudio);
            audioFiles.put("usaAudio", usaAudio);
            audioService.saveAudioFile(audioFiles, audio);
            return ResponseEntity.ok(audioService.saveTheEditedAudio(audio));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DtoTranslationPairToUI>> search(@RequestParam("searchTerm") String searchTerm) {

        if (!searchTerm.isBlank()) {
            List<DtoTranslationPairToUI> list = translationPairService.searchResult(searchTerm);
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search-word")
    public ResponseEntity<List<DtoWordToUI>> searchWord(@RequestParam("searchTerm") String searchTerm) {

        if (!searchTerm.isBlank()) {
            List<DtoWordToUI> list = wordService.searchWordToAdminPage(searchTerm);
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/page-phrases-save")
    public ResponseEntity<CustomResponseMessage> translationPairPageSave(@RequestBody DtoTranslationPairsPage dtoTranslationPairsPage,
                                                                         Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(translationPairPageService.saveTranslationPairsPage(dtoTranslationPairsPage));
        }
        return ResponseEntity.ok(new CustomResponseMessage(Message.ADD_BASE_SUCCESS));
    }

    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadWebImage(@RequestParam("webImage") MultipartFile file,
                                                 Principal principal) {
        if (principal != null) {
            String contentType = file.getContentType();
            if (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp")) {
                System.out.println("Yes");
                String fileName = imagesService.saveWebImage(file, contentType);
                return ResponseEntity.ok("/web-image/" + fileName);
            } else throw new FileFormatException("Дозволено тільки зображення");

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word-image/{id}/upload")
    public ResponseEntity<CustomResponseMessage> uploadWordImage(@PathVariable("id") Long wordId, @RequestParam("wordImage") MultipartFile file,
                                                                 Principal principal) {
        if (principal != null) {
            String contentType = file.getContentType();
            if (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp")) {
                return ResponseEntity.ok(imagesService.saveWordImage(file, wordId, contentType));
            } else throw new FileFormatException("Дозволено тільки зображення");

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/category-image/{id}/upload")
    public ResponseEntity<CustomResponseMessage> uploadCategoryImage(@PathVariable("id") Long categoryId, @RequestParam("categoryImage") MultipartFile file,
                                                                     Principal principal) {
        if (principal != null) {
            String contentType = file.getContentType();
            if (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp")) {
                System.out.println("Yes");
                return ResponseEntity.ok(imagesService.saveCategoryImage(file, categoryId, contentType));
            } else throw new FileFormatException("Дозволено тільки зображення");

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/save-wayforpay-module-settings")
    public ResponseEntity<CustomResponseMessage> saveSettingsWayForPayModule(@RequestBody WayForPayModule wayForPayModule,
                                                                             Principal principal) {
        if (principal != null) {
            wayForPayModule.setId(1L);
            if (wayForPayModule.isActive()) {
                if (!wayForPayModule.getMerchantSecretKey().isBlank() && !wayForPayModule.getMerchantAccount().isBlank()) {
                    wayForPayModuleService.saveWayForPayModule(wayForPayModule);
                    return ResponseEntity.ok(new CustomResponseMessage(Message.SUCCESS));
                } else {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR, "Поля не можуть бути пустими"));
                }
            } else {
                wayForPayModuleService.saveWayForPayModule(wayForPayModule);
                return ResponseEntity.ok(new CustomResponseMessage(Message.SUCCESS, "Налаштування збережені"));
            }
        }
        return ResponseEntity.notFound().build();
    }

}
