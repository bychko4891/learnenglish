package com.example.learnenglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.service.VocabularyPageService;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
public class VocabularyPageRestController {

    private final VocabularyPageService vocabularyPageService;

}
