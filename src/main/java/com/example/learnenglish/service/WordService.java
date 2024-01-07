package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.model.PhraseApplication;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.repository.WordRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;

    public Word getWord(Long id) {
        Optional<Word> wordOptional = wordRepository.findById(id);
        if (wordOptional.isPresent()) {
            return wordOptional.get();
        }
        throw new RuntimeException("");
    }

    public Word getNewWord(Long id) {
        Word word = new Word();
        word.setId(id);
        word.setName("Enter name");
//        word.setDescription("Enter text");
        return word;
    }


    public Page<Word> getWordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAll(pageable);
    }

    public Long countWords() {
        return wordRepository.lastId();
    }


    @Transactional
    public CustomResponseMessage saveWord(Word wordDB, Word word) {
        Optional.ofNullable(word.getName()).ifPresent(wordDB::setName);
        Optional.ofNullable(word.getTranslate()).ifPresent(wordDB::setTranslate);
//        Optional.ofNullable(word.getDescription()).ifPresent(wordDB::setDescription);
        Optional.ofNullable(word.getBrTranscription()).ifPresent(wordDB::setBrTranscription);
        Optional.ofNullable(word.getUsaTranscription()).ifPresent(wordDB::setUsaTranscription);
        Optional.ofNullable(word.getIrregularVerbPt()).ifPresent(wordDB::setIrregularVerbPt);
        Optional.ofNullable(word.getIrregularVerbPp()).ifPresent(wordDB::setIrregularVerbPp);
        wordDB.setActiveURL(word.isActiveURL());
        wordRepository.save(wordDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }


    @Transactional
    public CustomResponseMessage saveNewWord(Word word) {
//        word.setAudio(new Audio());


        /////////// Переробити !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        if (id != null && id.size() > 0) {
//            for (PhraseApplication arr : id) {
//                PhraseApplication pa = new PhraseApplication();
//                pa.setId(arr);
//                word.getPhraseExamples().add(pa);
//            }
//        }
        /////////////
        wordRepository.save(word);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }



    public Page<Word> getUserWords(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = wordRepository.findAll(pageable, userId);
        List<Word> words = new ArrayList<>();
        for (Object[] result : resultPage.getContent()) {
            Word word = (Word) result[0];
            Boolean isRepeatable = (Boolean) result[1];
            word.setRepeatable(isRepeatable);
            words.add(word);
        }
        return new PageImpl<>(words, pageable, resultPage.getTotalElements());
    }

    public List<DtoWordToUI> searchWord(String searchTerm) {
//        List<Word> wordsResult = wordRepository.findWord(searchTerm);
//        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
//        for (Word arr : wordsResult) {
//            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));
//        }
//        return dtoWordToUIList;
        return null;
    }

    @Transactional
    public List<DtoWordToUI> searchWordToAdminPage(String searchTerm) {
//        List<Word> wordsResult = wordRepository.findWordToAdminPage(searchTerm);
//        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
//        for (Word arr : wordsResult) {
//            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));
//        }
//        return dtoWordToUIList;
        return null;
    }

    @Transactional
    public List<DtoWordToUI> searchWordForPhraseApplication(String searchTerm) {
        List<Word> wordsResult = wordRepository.findWordForPhraseApplication(searchTerm);
        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
        for (Word arr : wordsResult) {
            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));
        }
        return dtoWordToUIList;
    }

    public Page<Word> wordsFromLesson(int page, int size, Long wordLessonId) {
//        Pageable pageable = PageRequest.of(page, size);
//        return wordRepository.wordsFromLesson(pageable, wordLessonId);
        return null;
    }

    public CustomResponseMessage confirmWord(String wordConfirm, Long id) {
        Optional<Word> wordOptional = wordRepository.findById(id);
        if (wordOptional.isPresent()) {
            Word word = wordOptional.get();
            if (word.getName().equals(StringUtils.normalizeSpace(wordConfirm))) {
                return new CustomResponseMessage(Message.SUCCESS, word.getName());
            } else return new CustomResponseMessage(Message.ERROR, word.getName());
        } else return new CustomResponseMessage(Message.BASE_ERROR);
    }

    public List<DtoWordToUI> wordsToAudit(List<Long> wordsId, int wordAuditCounter) {
        List<Word> words = wordRepository.findByIds(wordsId);
        List<DtoWordToUI> wordToUIS = new ArrayList<>();
        for (Word arr : words) {
            wordToUIS.add(DtoWordToUI.convertToDTO(arr));
            wordToUIS.get(wordToUIS.size() - 1).setTotalPage(wordAuditCounter);
        }
        return wordToUIS;
    }

    public DtoWordToUI getWordForWordLessonAudit(Long wordId, int wordAuditCounter, int wordsIdListLength) {
        Word word = getWord(wordId);
        DtoWordToUI dtoWordToUI = DtoWordToUI.convertToDTO(word);
        dtoWordToUI.setTotalPage(wordAuditCounter - 1);
        int count = (int) (Math.random() * 10);
        if (count % 2 != 0 && wordsIdListLength > 2) {
            dtoWordToUI.setWordAuditSlide("slideAuditRadios");
        }
        return dtoWordToUI;
    }


}
