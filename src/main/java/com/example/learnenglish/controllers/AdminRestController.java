package com.example.learnenglish.controllers;
/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoTextOfAppPage;
import com.example.learnenglish.dto.DtoWord;
import com.example.learnenglish.dto.DtoWordsCategory;
import com.example.learnenglish.dto.DtoWordsCategoryToUi;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin-page")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminRestController {
    private final LessonService lessonService;
    private final UserService userService;
    private final TextOfAppPageService textOfAppPageService;
    private final CategoryService wordCategoryService;
    private final WordService wordService;
    private final AudioService wordAudioService;

    public AdminRestController(LessonService lessonService,
                               UserService userService,
                               TextOfAppPageService textOfAppPageService,
                               CategoryService wordCategoryService,
                               WordService wordService,
                               AudioService wordAudioService) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.textOfAppPageService = textOfAppPageService;
        this.wordCategoryService = wordCategoryService;
        this.wordService = wordService;
        this.wordAudioService = wordAudioService;
    }

    @PostMapping("/text-of-app-page/{id}/edit")
    public ResponseEntity<ResponseMessage> createAppTextPage(@RequestBody DtoTextOfAppPage dtoTextOfAppPage,
                                                             Principal principal) {
        if (principal != null) {
//            textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage);
            return ResponseEntity.ok(textOfAppPageService.textOfAppPageEdit(dtoTextOfAppPage));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/lessons/lesson/{id}/edit")
    public ResponseEntity<String> lessonsEdit(@PathVariable("id") Long id,
                                              @RequestBody Lesson lesson,
                                              Principal principal) {
//        model.addAttribute("lesson", lesson);
        if (principal != null) {
            id = lesson.getId();
            lessonService.lessonEdit(lesson);
            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.ok("No");
    }


    @PostMapping("/users/user/user-active")
    @ResponseBody
    public void userFieldActive(@RequestParam("userActive") boolean userActive,
                                @RequestParam("userId") Long userId,
                                Principal principal) {
        if (principal != null) {
            userService.userActiveEdit(userId, userActive);
            System.out.println(userActive);
//                return ResponseEntity.ok(new ResponseStatus(Message.SUCCESS_CREATELESSON));
        }
//        return ResponseEntity.ok(new ResponseStatus(Message.ERROR_CREATELESSON));
    }

    @GetMapping("/getSubcategories")
    public ResponseEntity<List<DtoWordsCategoryToUi>> wordsSubcategories(@RequestParam("mainCategoryId") Long id, Principal principal) {
        if (principal != null && id != 0) {
            return ResponseEntity.ok(wordCategoryService.getSubcategoriesInMainCategory(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/category-save")
    public ResponseEntity<ResponseMessage> SaveWordsCategory(@RequestBody DtoWordsCategory dtoWordsCategory,
                                                              Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(wordCategoryService.saveCategory(dtoWordsCategory));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/word-save")
    public ResponseEntity<ResponseMessage> saveWord(@RequestBody DtoWord dtoWord,
                                                              Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(wordService.saveWord(dtoWord));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/word-audio/{id}/upload")
    public ResponseEntity<ResponseMessage> saveWord(@PathVariable("id") Long wordId,
                                                    @RequestParam("brAudio") MultipartFile brAudio,
                                                    @RequestParam("usaAudio") MultipartFile usaAudio,
                                                    Principal principal) {
        if (principal != null) {
            wordAudioService.saveAudioFile(brAudio, usaAudio,wordId);
//            MultipartFile brAudioFile = brAudio;
//            MultipartFile usaAudioFile = usaAudio;
            return ResponseEntity.ok(new ResponseMessage(Message.SUCCESSADDBASE));
        }
        return ResponseEntity.notFound().build();
    }
}
