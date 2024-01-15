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
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VocabularyPageService {

    private final VocabularyPageRepository repository;

    public VocabularyPage getVocabularyPage(long id) {
        Optional<VocabularyPage> vocabularyPageOptional = repository.findById(id);
        if (vocabularyPageOptional.isPresent()) {
            return vocabularyPageOptional.get();
        }
        throw new RuntimeException("");
    }

    public VocabularyPage getNewVocabularyPage(long id) {
        VocabularyPage vocabularyPage= new VocabularyPage();
        vocabularyPage.setId(id);
        vocabularyPage.setName("name");
        vocabularyPage.setDescription("Enter text");
        return vocabularyPage;
    }


    public Page<VocabularyPage> getVocabularyPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllVocabularyPage(pageable);
    }

    public long countVocabularyPages() {
        return repository.lastId();
    }

    @Transactional
    public CustomResponseMessage saveVocabularyPage(VocabularyPage vocabularyPageDB, VocabularyPage vocabularyPage) {
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

    public CustomResponseMessage saveNewVocabularyPage(VocabularyPage vocabularyPage) {
        String vocabularyPageName = StringUtils.normalizeSpace(vocabularyPage.getName());
        vocabularyPage.setName(vocabularyPageName);
        if(vocabularyPage.getCategory().getId() == 0) vocabularyPage.setCategory(null);
        repository.save(vocabularyPage);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

}
