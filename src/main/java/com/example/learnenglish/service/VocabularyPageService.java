package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.VocabularyPage;
import com.example.learnenglish.repository.VocabularyPageRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VocabularyPageService {

    private final VocabularyPageRepository repository;

    @Transactional
    public CustomResponseMessage saveWord(VocabularyPage vocabularyPageDB, VocabularyPage vocabularyPage) {
//        Optional.ofNullable(word.getName()).ifPresent(wordDB::setName);
//        Optional.ofNullable(word.getTranslate()).ifPresent(wordDB::setTranslate);
//        Optional.ofNullable(word.getDescription()).ifPresent(wordDB::setDescription);
//        Optional.ofNullable(word.getBrTranscription()).ifPresent(wordDB::setBrTranscription);
//        Optional.ofNullable(word.getUsaTranscription()).ifPresent(wordDB::setUsaTranscription);
//        Optional.ofNullable(word.getIrregularVerbPt()).ifPresent(wordDB::setIrregularVerbPt);
//        Optional.ofNullable(word.getIrregularVerbPp()).ifPresent(wordDB::setIrregularVerbPp);
//        Optional.ofNullable(word.getInfo()).ifPresent(wordDB::setInfo);
//        wordDB.setPublished(word.isPublished());
//        if (word.getCategory() != null && word.getCategory().getId() != 0) {
//            if (wordDB.getCategory() == null || !word.getCategory().getId().equals(wordDB.getCategory().getId())) {
//                wordDB.setCategory(word.getCategory());
//            }
//        }
//        wordRepository.save(wordDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }

}