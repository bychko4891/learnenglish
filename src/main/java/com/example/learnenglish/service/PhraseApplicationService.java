package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.PhraseApplication;
import com.example.learnenglish.model.WordWithOrder;
import com.example.learnenglish.repository.PhraseApplicationRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhraseApplicationService {

    private final PhraseApplicationRepository repository;

    public Long countPhraseApplication() {
        return repository.lastId();
    }

    public PhraseApplication getPhraseApplication(Long id) {
        Optional<PhraseApplication> phraseApplicationOptional = repository.findById(id);
        if (phraseApplicationOptional.isPresent()) return phraseApplicationOptional.get();
        throw new RuntimeException("");
    }

    public PhraseApplication newPhraseApplication(Long id) {
        PhraseApplication phraseApplication = new PhraseApplication();
        phraseApplication.setId(id);
        phraseApplication.setUkrTranslate("Enter translate");
        return phraseApplication;
    }

    public Page<PhraseApplication> getAllPhraseApplication(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllPhraseApplicationForAdmin(pageable);
    }

    @Transactional
    public CustomResponseMessage saveNewPhraseApplication(PhraseApplication phraseApplication) {
        List<WordWithOrder> wwo = phraseApplication.getEngPhrase();
        for (int i = 0; i < wwo.size(); i++) {
            wwo.get(i).setPhraseApplication(phraseApplication);
        }
        phraseApplication.setEngPhrase(wwo);
        repository.save(phraseApplication);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
    }

    @Transactional
    public CustomResponseMessage savePhraseApplication(PhraseApplication phraseApplicationDB, PhraseApplication phraseApplication) {
        Optional.ofNullable(phraseApplication.getUkrTranslate()).ifPresent(phraseApplicationDB::setUkrTranslate);
        Optional.of(phraseApplication.isQuestionForm()).ifPresent(phraseApplicationDB::setQuestionForm);
        phraseApplicationDB.getEngPhrase().clear();
        List<WordWithOrder> listUI = phraseApplication.getEngPhrase();
        for (int i = 0; i < listUI.size(); i++) {
            listUI.get(i).setPhraseApplication(phraseApplicationDB);
            phraseApplicationDB.getEngPhrase().add(listUI.get(i));
        }
        repository.save(phraseApplicationDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
    }
}
