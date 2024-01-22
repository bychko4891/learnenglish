package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.VocabularyPage;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.repository.VocabularyPageRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VocabularyPageService {

    private final VocabularyPageRepository vocabularyPageRepository;

    private final WordService wordService;

    public VocabularyPage getVocabularyPage(long id) {
        Optional<VocabularyPage> vocabularyPageOptional = vocabularyPageRepository.findById(id);
        if (vocabularyPageOptional.isPresent()) {
            return vocabularyPageOptional.get();
        } else throw new RuntimeException("");
    }

    public VocabularyPage getNewVocabularyPage(long id) {
        VocabularyPage vocabularyPage= new VocabularyPage();
        vocabularyPage.setId(id);
        vocabularyPage.setDescription("Enter text");
        return vocabularyPage;
    }


    public Page<VocabularyPage> getVocabularyPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vocabularyPageRepository.findAllVocabularyPage(pageable);
    }

    public long countVocabularyPages() {
        return vocabularyPageRepository.lastId();
    }

    @Transactional
    public CustomResponseMessage saveVocabularyPage(VocabularyPage vocabularyPageDB, VocabularyPage vocabularyPage) {
        if(!vocabularyPage.getWord().getId().equals(vocabularyPageDB.getWord().getId())) {
            Word word = wordService.getWord(vocabularyPage.getWord().getId());
            vocabularyPageDB.setWord(word);
            vocabularyPageDB.setName(word.getName());
        }
        Optional.ofNullable(vocabularyPage.getImage().getImageName())
                .ifPresent(imageName -> vocabularyPageDB.getImage().setImageName(imageName));
        Optional.ofNullable(vocabularyPage.getCardInfo()).ifPresent(vocabularyPageDB::setCardInfo);
        Optional.ofNullable(vocabularyPage.getDescription()).ifPresent(vocabularyPageDB::setDescription);
        vocabularyPageDB.setPublished(vocabularyPage.isPublished());
        if (vocabularyPage.getCategory() != null && vocabularyPage.getCategory().getId() != 0) {
            if (vocabularyPageDB.getCategory() == null || !vocabularyPage.getCategory().getId().equals(vocabularyPageDB.getCategory().getId())) {
                vocabularyPageDB.setCategory(vocabularyPage.getCategory());
            }
        }
        vocabularyPageRepository.save(vocabularyPageDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }

    @Transactional
    public CustomResponseMessage saveNewVocabularyPage(VocabularyPage vocabularyPage) {
        Word word = wordService.getWord(vocabularyPage.getWord().getId());
        vocabularyPage.setName(word.getName());
        vocabularyPage.setWord(word);
        if(vocabularyPage.getCategory().getId() == 0) vocabularyPage.setCategory(null);
        if(vocabularyPage.getWord().getId() == null ) vocabularyPage.setWord(null);
        //TODO : Add phrases Application
        vocabularyPageRepository.save(vocabularyPage);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public boolean existVocabularyPageByName(String vocabularyPageName) {
        String vocabularyPageNameNormalize = StringUtils.normalizeSpace(vocabularyPageName);
        return vocabularyPageRepository.existsVocabularyPageByNameIgnoreCase(vocabularyPageNameNormalize);
    }

    @Transactional
    public List<VocabularyPage> searchVocabularyPageForWordLesson(String searchTerm) {
        return vocabularyPageRepository.findVocabularyPageForWordLesson(searchTerm);
    }

}
