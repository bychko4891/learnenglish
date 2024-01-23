package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Word;
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
        } else throw new RuntimeException("");
    }

    public Word getNewWord(Long id) {
        Word word = new Word();
        word.setId(id);
        word.setName("name");
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
        String wordName = StringUtils.normalizeSpace(word.getName()).replaceAll("\\s{2,}", " ");;
        Optional <Word> wordDuplicate = wordRepository.findWordByNameEqualsIgnoreCase(wordName);
        if (wordDuplicate.isEmpty() || wordDuplicate.get().getId().equals(wordDB.getId())) {
            if(wordDB.getAudio().getUsaAudioName() != null &&  wordDB.getAudio().getBrAudioName().equals(wordDB.getAudio().getUsaAudioName())
                    && word.getAudio().getUsaAudioName() == null && word.getAudio().getBrAudioName() != null
                    || word.getAudio().getUsaAudioName() != null && word.getAudio().getBrAudioName() == null) {
                if(word.getAudio().getBrAudioName() != null) {
                    wordDB.getAudio().setBrAudioName(word.getAudio().getBrAudioName());
                    wordDB.getAudio().setUsaAudioName(word.getAudio().getBrAudioName());
                }
                if(word.getAudio().getUsaAudioName() != null) {
                    wordDB.getAudio().setBrAudioName(word.getAudio().getUsaAudioName());
                    wordDB.getAudio().setUsaAudioName(word.getAudio().getUsaAudioName());
                }
            }
            Optional.ofNullable(word.getAudio().getBrAudioName()).ifPresent(audioName -> wordDB.getAudio().setBrAudioName(audioName));
            Optional.ofNullable(word.getAudio().getUsaAudioName()).ifPresent(audioName -> wordDB.getAudio().setUsaAudioName(audioName));

            if (wordDB.getAudio().getUsaAudioName() == null && word.getAudio().getBrAudioName() != null)
                wordDB.getAudio().setUsaAudioName(word.getAudio().getBrAudioName());

            if (wordDB.getAudio().getBrAudioName() == null && word.getAudio().getUsaAudioName() != null)
                wordDB.getAudio().setBrAudioName(word.getAudio().getUsaAudioName());

//            Optional.ofNullable(wordName).filter(name -> !name.isEmpty()).ifPresent(wordDB::setName);
            Optional.of(wordName).ifPresent(wordDB::setName);
            Optional.ofNullable(word.getTranslate()).ifPresent(wordDB::setTranslate);
            Optional.ofNullable(word.getBrTranscription()).ifPresent(wordDB::setBrTranscription);
            Optional.ofNullable(word.getUsaTranscription()).ifPresent(wordDB::setUsaTranscription);
            Optional.ofNullable(word.getIrregularVerbPt()).ifPresent(wordDB::setIrregularVerbPt);
            Optional.ofNullable(word.getIrregularVerbPp()).ifPresent(wordDB::setIrregularVerbPp);
            wordDB.setActiveURL(word.isActiveURL());
            wordRepository.save(wordDB);
            return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
        }
        return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);
    }


    @Transactional
    public CustomResponseMessage saveNewWord(Word word) {
        String wordName = StringUtils.normalizeSpace(word.getName()).replaceAll("\\s{2,}", " ");
        if (!wordRepository.existsWordByNameEqualsIgnoreCase(wordName)) {
            if (word.getAudio().getUsaAudioName() == null || word.getAudio().getBrAudioName() == null) {
                if (word.getAudio().getUsaAudioName() == null)
                    word.getAudio().setUsaAudioName(word.getAudio().getBrAudioName());
                if (word.getAudio().getBrAudioName() == null)
                    word.getAudio().setBrAudioName(word.getAudio().getUsaAudioName());
            }
            word.setName(wordName);
            wordRepository.save(word);
            return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
        }
        return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);
    }


    public Page<Word> getUserWords(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = wordRepository.findAll(pageable, userId);
        List<Word> words = new ArrayList<>();
        for (Object[] result : resultPage.getContent()) {
            Word word = (Word) result[0];
            Boolean isRepeatable = (Boolean) result[1];
//            word.setRepeatable(isRepeatable);
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
    public List<Word> searchWordToAdminPage(String searchTerm) {
//        return wordRepository.findWordToAdmin(searchTerm);
        return null;
    }

    @Transactional
    public List<Word> searchWordForVocabularyPage(String searchTerm) {
        return wordRepository.findWordForVocabularyPage(searchTerm);
    }

    @Transactional
    public List<Word> searchWordForPhraseApplication(String searchTerm) {
        return wordRepository.findWordForPhraseApplication(searchTerm);
    }





}
