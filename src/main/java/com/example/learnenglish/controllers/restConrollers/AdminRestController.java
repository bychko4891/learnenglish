package com.example.learnenglish.controllers.restConrollers;
/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.*;
import com.example.learnenglish.exception.FileFormatException;
import com.example.learnenglish.model.*;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminRestController {
    private final PhraseLessonService phraseLessonService;
    private final UserService userService;
    private final TextOfAppPageService textOfAppPageService;
    private final CategoryService categoryService;
    private final WordService wordService;
    private final AudioService audioService;
    private final TranslationPairService translationPairService;
    private final ImagesService imagesService;
    private final MiniStoryService miniStoryService;
    private final WordLessonService wordLessonService;
    private final WayForPayModuleService wayForPayModuleService;
    private final FileStorageService fileStorageService;

    private final PhraseApplicationService phraseApplicationService;


    @PostMapping("/text-of-app-page/{id}/edit")
    public ResponseEntity<CustomResponseMessage> createAppTextPage(@RequestBody DtoTextOfAppPage dtoTextOfAppPage,
                                                                   Principal principal) {
        if (principal != null) {
//            textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage);
            return ResponseEntity.ok(textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage));
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



    @PostMapping("/word-lesson-save")
    public ResponseEntity<CustomResponseMessage> saveWordLesson(@RequestBody WordLesson wordLesson,
                                                                Principal principal) {
        if (principal != null) {
            try {
                WordLesson wordLessonDB = wordLessonService.getWordLesson(wordLesson.getId());
                return ResponseEntity.ok(wordLessonService.saveWordLesson(wordLessonDB, wordLesson));
            } catch (RuntimeException e) {
                return ResponseEntity.ok(wordLessonService.saveNewWordLesson(wordLesson));
            }

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/phrase-application-save")
    public ResponseEntity<CustomResponseMessage> savePhraseApplication(@RequestBody PhraseApplication phraseApplication,
                                                                Principal principal) {
        if (principal != null) {
            try {
                PhraseApplication phraseApplicationDB = phraseApplicationService.getPhraseApplication(phraseApplication.getId());
                return ResponseEntity.ok(phraseApplicationService.savePhraseApplication(phraseApplicationDB, phraseApplication));
            } catch (RuntimeException e) {
               return ResponseEntity.ok(phraseApplicationService.saveNewPhraseApplication(phraseApplication));
            }
//            return ResponseEntity.ok(wordLessonService.saveWordLesson(dtoWordLesson));
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


    @PostMapping("/page-phrases-save")
    public ResponseEntity<CustomResponseMessage> translationPairPageSave(@RequestBody DtoTranslationPairsPage dtoTranslationPairsPage,
                                                                         Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(miniStoryService.saveTranslationPairsPage(dtoTranslationPairsPage));
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
