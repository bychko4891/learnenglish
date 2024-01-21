package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTextOfAppPage;
import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.dto.DtoTranslationPairsPage;
import com.example.learnenglish.model.WayForPayModule;
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminRestController {
    private final UserService userService;
    private final TextOfAppPageService textOfAppPageService;
    private final TranslationPairService translationPairService;
    private final MiniStoryService miniStoryService;
    private final WordLessonService wordLessonService;
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
