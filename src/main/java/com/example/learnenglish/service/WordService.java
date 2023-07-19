package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.DtoWord;
import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.Audio;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.TranslationPairRepository;
import com.example.learnenglish.repository.WordRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class WordService {
    private final WordRepository wordRepository;
    private final CategoryRepository wordCategoryRepository;
    private final TranslationPairRepository translationPairRepository;
    private final UserService userService;

    public WordService(WordRepository wordRepository,
                       CategoryRepository wordCategoryRepository,
                       TranslationPairRepository translationPairRepository,
                       UserService userService) {
        this.wordRepository = wordRepository;
        this.wordCategoryRepository = wordCategoryRepository;
        this.translationPairRepository = translationPairRepository;
        this.userService = userService;
    }

    public Page<Word> getWordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAll(pageable);
    }

    public Long countWords() {
        return wordRepository.count();
    }

    public ResponseMessage saveWord(Long userId, DtoWord dtoWord) {
        Optional<Word> wordOptional = wordRepository.findById(dtoWord.getWord().getId());
        Long categoryId = dtoWord.getSubSubcategorySelect().getId() != 0 ? dtoWord.getSubSubcategorySelect().getId() :
                dtoWord.getSubcategorySelect().getId() != 0 ? dtoWord.getSubcategorySelect().getId() :
                        dtoWord.getMainCategorySelect().getId() != 0 ? dtoWord.getMainCategorySelect().getId() : 0;
        if (wordOptional.isPresent()) {
            Word word = wordOptional.get();
            word.setName(dtoWord.getWord().getName());
            word.getAudio().setName(dtoWord.getWord().getName());
            word.setTranslate(dtoWord.getWord().getTranslate());
            word.setBrTranscription(dtoWord.getWord().getBrTranscription());
            word.setUsaTranscription(dtoWord.getWord().getUsaTranscription());
            word.setIrregularVerbPt(dtoWord.getWord().getIrregularVerbPt());
            word.setIrregularVerbPp(dtoWord.getWord().getIrregularVerbPp());
            word.setPublished(dtoWord.getWord().isPublished());
            word.setText(dtoWord.getWord().getText());
            List<TranslationPair> translationPairs = word.getTranslationPairs();
            if (translationPairs.size() != 0 && translationPairs.size() != dtoWord.getWord().getTranslationPairs().size()) {
                List<TranslationPair> dtoTranslationPairs = dtoWord.getWord().getTranslationPairs();
                Iterator<TranslationPair> iterator = translationPairs.iterator();
                while (iterator.hasNext()) {
                    TranslationPair pair = iterator.next();
                    boolean containsId = false;
                    for (TranslationPair arr : dtoTranslationPairs) {
                        if (pair.getId() == arr.getId()) {
                            containsId = true;
                            break;
                        }
                    }
                    if (!containsId) {
                        iterator.remove();
                    }
                }
            }
            if (dtoWord.getTranslationPairsId().size() != 0) {
                List<TranslationPair> list = translationPairRepository.findByIds(dtoWord.getTranslationPairsId());
                for (TranslationPair arr : list) {
                    word.getTranslationPairs().add(arr);
                }
            }
            word.setTranslationPairs(translationPairs);
            if(categoryId != 0 && word.getWordCategory() == null){
                Category wordCategory = wordCategoryRepository.findById(categoryId).get();
                word.setWordCategory(wordCategory);
                wordCategory.getWords().add(word);
            } else if (categoryId != 0 && word.getWordCategory().getId() != categoryId) {
                Category wordCategoryRemove = word.getWordCategory();
                wordCategoryRemove.getWords().removeIf(obj -> obj.getId().equals(word.getId()));
                word.setWordCategory(wordCategoryRepository.findById(categoryId).get());
            }
            wordRepository.save(word);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else return saveNewWord(userId, dtoWord, categoryId);
    }

    private ResponseMessage saveNewWord(Long userId, DtoWord dtoWord, Long categoryId){
        Word word = new Word();
        Audio audio = new Audio();
//        User user = userService.findById(userId);
//        word.setUser(user);
        word.setName(dtoWord.getWord().getName());
        word.setTranslate(dtoWord.getWord().getTranslate());
        word.setPublished(dtoWord.getWord().isPublished());
        word.setText(dtoWord.getWord().getText());
        word.setBrTranscription(dtoWord.getWord().getBrTranscription());
        word.setUsaTranscription(dtoWord.getWord().getUsaTranscription());
        word.setIrregularVerbPt(dtoWord.getWord().getIrregularVerbPt());
        word.setIrregularVerbPp(dtoWord.getWord().getIrregularVerbPp());
        audio.setName(dtoWord.getWord().getName());
        word.setAudio(audio);
        if (dtoWord.getTranslationPairsId().size() != 0) {
            List<TranslationPair> list = translationPairRepository.findByIds(dtoWord.getTranslationPairsId());
//            for (TranslationPair arr : list) {
//                arr.getWords().add(word);
//            }
            word.setTranslationPairs(list);
        }
        if(categoryId != 0){
            Category wordCategory = wordCategoryRepository.findById(categoryId).get();
            word.setWordCategory(wordCategory);
            wordCategory.getWords().add(word);
        }
        wordRepository.save(word);
        return new ResponseMessage(Message.SUCCESSADDBASE);
    }

    public Word getWord(Long id) {
        Optional<Word> wordOptional = wordRepository.findById(id);
        if(wordOptional.isPresent()){
            return wordOptional.get();
        }
        throw new RuntimeException("Error in method 'getWordToEditor' class 'WordService'");
    }

    public List<DtoWordToUI> searchWord(String searchTerm) {
        List<Word> wordsResult = wordRepository.findWord(searchTerm);
        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
        for (Word arr: wordsResult) {
            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));

        }
        return dtoWordToUIList;
    }

}
