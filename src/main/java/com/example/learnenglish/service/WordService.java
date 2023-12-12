package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.WordDto;
import com.example.learnenglish.dto.DtoWordToUI;
import com.example.learnenglish.mapper.WordMapper;
import com.example.learnenglish.model.*;
import com.example.learnenglish.model.users.Image;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.PhraseUserRepository;
import com.example.learnenglish.repository.WordRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
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


    public Page<Word> getWordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAll(pageable);
    }

    public Long countWords() {
        return wordRepository.lastId();
    }


    @Transactional
    public CustomResponseMessage saveWord(Word wordDB, WordDto wordDto) {
        Optional.ofNullable(wordDto.getWord().getName()).ifPresent(wordDB::setName);
        Optional.ofNullable(wordDto.getWord().getTranslate()).ifPresent(wordDB::setTranslate);
        Optional.ofNullable(wordDto.getWord().getDescription()).ifPresent(wordDB::setDescription);
        Optional.ofNullable(wordDto.getWord().getBrTranscription()).ifPresent(wordDB::setBrTranscription);
        Optional.ofNullable(wordDto.getWord().getUsaTranscription()).ifPresent(wordDB::setUsaTranscription);
        Optional.ofNullable(wordDto.getWord().getIrregularVerbPt()).ifPresent(wordDB::setIrregularVerbPt);
        Optional.ofNullable(wordDto.getWord().getIrregularVerbPp()).ifPresent(wordDB::setIrregularVerbPp);
        Optional.ofNullable(wordDto.getWord().getInfo()).ifPresent(wordDB::setInfo);
        wordDB.setPublished(wordDto.getWord().isPublished());
        if (wordDto.getWord().getCategory() != null && wordDto.getWord().getCategory().getId() != 0) {
            if (wordDB.getCategory() == null || !wordDto.getWord().getCategory().getId().equals(wordDB.getCategory().getId())) {
                wordDB.setCategory(wordDto.getWord().getCategory());
            }
        }
        wordRepository.save(wordDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }


    @Transactional
    public CustomResponseMessage saveNewWord(WordDto wordDto) {
        List<Long> id = wordDto.getPhrasesApplicationId();
        Word word = wordDto.getWord();
        word.setImages(new Image());
        word.setAudio(new Audio());
        if (word.getCategory().getId() == 0) {
            word.setCategory(null);
        }
        if(id != null && id.size() > 0) {
            for (Long arr : id) {
                PhraseApplication pa = new PhraseApplication();
                pa.setId(arr);
                word.getPhraseExamples().add(pa);
            }
        }
        wordRepository.save(wordDto.getWord());
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);

//        Optional<Word> wordOptional = wordRepository.findById(dtoWord.getWord().getId());
//        if (wordOptional.isPresent()) {
//            Word word = wordOptional.get();
//            DtoWord.convertDtoToWord(dtoWord, word);
//            List<PhraseUser> phraseUsers = word.getPhraseUsers();
//            if (phraseUsers.size() != 0 && phraseUsers.size() != dtoWord.getWord().getPhraseUsers().size()) {
//                List<PhraseUser> dtoPhraseUsers = dtoWord.getWord().getPhraseUsers();
//                Iterator<PhraseUser> iterator = phraseUsers.iterator();
//                while (iterator.hasNext()) {
//                    PhraseUser pair = iterator.next();
//                    boolean containsId = false;
//                    for (PhraseUser arr : dtoPhraseUsers) {
//                        if (pair.getId().equals(arr.getId())) {
//                            containsId = true;
//                            break;
//                        }
//                    }
//                    if (!containsId) {
//                        iterator.remove();
//                    }
//                }
//            }
//            if (dtoWord.getTranslationPairsId().size() != 0) {
//                List<PhraseUser> list = phraseUserRepository.findByIds(dtoWord.getTranslationPairsId());
//                for (PhraseUser arr : list) {
//                    word.getPhraseUsers().add(arr);
//                }
//            }
//            word.setPhraseUsers(phraseUsers);
//            if (categoryId != 0 && word.getCategory() == null) {
//                Category wordCategory = wordCategoryRepository.findById(categoryId).get();
//                word.setCategory(wordCategory);
//                wordCategory.getWords().add(word);
//            } else if (categoryId != 0 && !word.getCategory().getId().equals(categoryId)) {
//                Category wordCategoryRemove = word.getCategory();
//                wordCategoryRemove.getWords().removeIf(obj -> obj.getId().equals(word.getId()));
//                word.setCategory(wordCategoryRepository.findById(categoryId).get());
//            }
//            wordRepository.save(word);
//            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
//        } else return saveNewWord(dtoWord, categoryId);

    }

    private CustomResponseMessage saveNewWord(WordDto wordDto, Long categoryId) {

        return null;
    }
//    private CustomResponseMessage saveNewWord(WordDto wordDto, Long categoryId) {
//        if(!wordRepository.existsByNameIsIgnoreCase(dtoWord.getWord().getName().trim())) {
//            Word word = new Word();
//            Audio audio = new Audio();
//            Image images = new Image();
//            word.setAudio(audio);
//            word.setImages(images);
//            DtoWord.convertDtoToWord(dtoWord, word);
//            if (dtoWord.getTranslationPairsId().size() != 0) {
//                List<PhraseUser> list = phraseUserRepository.findByIds(dtoWord.getTranslationPairsId());
//                word.setPhraseUsers(list);
//            }
//            if (categoryId != 0) {
//                Category wordCategory = wordCategoryRepository.findById(categoryId).get();
//                word.setCategory(wordCategory);
//                wordCategory.getWords().add(word);
//            }
//            wordRepository.save(word);
//            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
//        }
//        return new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT);
//        return null;
//    }

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
        word.setInfo("Enter text");
        return word;
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
        List<Word> wordsResult = wordRepository.findWord(searchTerm);
        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
        for (Word arr : wordsResult) {
            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));
        }
        return dtoWordToUIList;
    }

    @Transactional
    public List<DtoWordToUI> searchWordToAdminPage(String searchTerm) {
        List<Word> wordsResult = wordRepository.findWordToAdminPage(searchTerm);
        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
        for (Word arr : wordsResult) {
            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));
        }
        return dtoWordToUIList;
    }

    public Page<Word> wordsFromLesson(int page, int size, Long wordLessonId) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.wordsFromLesson(pageable, wordLessonId);
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
