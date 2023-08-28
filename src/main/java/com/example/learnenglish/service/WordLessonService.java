package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoWordLesson;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.WordLessonRepository;
import com.example.learnenglish.repository.WordRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordLessonService {

    private final WordLessonRepository wordLessonRepository;
    private final CategoryRepository categoryRepository;
    private final WordRepository wordRepository;

    public Page<WordLesson> getWordLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordLessonRepository.findAll(pageable);
    }

    public Long countWordLesson() {
        return wordLessonRepository.lastId();
    }

    public WordLesson getWordLesson(Long id) {
        Optional<WordLesson> wordLessonOptional = wordLessonRepository.findById(id);
        if (wordLessonOptional.isPresent()) {
            return wordLessonOptional.get();
        } else {
            WordLesson wordLesson = new WordLesson();
            wordLesson.setName("Enter new name");
            wordLesson.setDescription("Enter description");
            wordLesson.setId(id);
            return wordLesson;
        }

    }

    public ResponseMessage saveWordLesson(DtoWordLesson dtoWordLesson) {
        Optional<WordLesson> wordLessonOptional = wordLessonRepository.findById(dtoWordLesson.getWordLesson().getId());
        Long categoryId = dtoWordLesson.getSubSubcategorySelect().getId() != 0 ? dtoWordLesson.getSubSubcategorySelect().getId() :
                dtoWordLesson.getSubcategorySelect().getId() != 0 ? dtoWordLesson.getSubcategorySelect().getId() :
                        dtoWordLesson.getMainCategorySelect().getId() != 0 ? dtoWordLesson.getMainCategorySelect().getId() : 0;
        if (wordLessonOptional.isPresent()) {
            WordLesson wordLesson = wordLessonOptional.get();
            wordLesson.setName(dtoWordLesson.getWordLesson().getName());
            wordLesson.setDescription(dtoWordLesson.getWordLesson().getDescription());
            wordLesson.setSerialNumber(dtoWordLesson.getWordLesson().getSerialNumber());
            List<Word> words = wordLesson.getWords();
            if (words.size() != 0 && words.size() != dtoWordLesson.getWordLesson().getWords().size()) {
                List<Word> dtoWords = dtoWordLesson.getWordLesson().getWords();
                Iterator<Word> iterator = words.iterator();
                while (iterator.hasNext()) {
                    Word word = iterator.next();
                    boolean containsId = false;
                    for (Word arr : dtoWords) {
                        if (word.getId() == arr.getId()) {
                            containsId = true;
                            break;
                        }
                    }
                    if (!containsId) {
                        iterator.remove();
                        word.setWordLesson(null);
                    }
                }
            }
            if (dtoWordLesson.getWordsId().size() != 0) {
                List<Word> list = wordRepository.findByIds(dtoWordLesson.getWordsId());
                for (Word arr : list) {
                    wordLesson.getWords().add(arr);
                    arr.setWordLesson(wordLesson);
                }
            }
            wordLesson.setWords(words);
            if (categoryId != 0 && wordLesson.getCategory() == null) {
                Category category = categoryRepository.findById(categoryId).get();
                wordLesson.setCategory(category);
            } else if (categoryId != 0 && wordLesson.getCategory().getId() != categoryId) {
                wordLesson.setCategory(categoryRepository.findById(categoryId).get());
            }
            wordLessonRepository.save(wordLesson);
            return new ResponseMessage(Message.SUCCESSADDBASE);
        } else return saveNewWordLesson(dtoWordLesson, categoryId);

    }

    private ResponseMessage saveNewWordLesson(DtoWordLesson dtoWordLesson, Long categoryId) {
        WordLesson wordLesson = new WordLesson();
        wordLesson.setName(dtoWordLesson.getWordLesson().getName());
        wordLesson.setDescription(dtoWordLesson.getWordLesson().getDescription());
        if (dtoWordLesson.getWordsId().size() != 0) {
            List<Word> list = wordRepository.findByIds(dtoWordLesson.getWordsId());
            for (Word arr : list) {
                arr.setWordLesson(wordLesson);
            }
            wordLesson.setWords(list);
        }
        if (categoryId != 0) {
            Category category = categoryRepository.findById(categoryId).get();
            wordLesson.setCategory(category);
        }
        wordLesson.setSerialNumber(dtoWordLesson.getWordLesson().getSerialNumber());
        if (dtoWordLesson.getWordLesson().getSerialNumber() == 0) {
            wordLesson.setSerialNumber(1000);
        }
        wordLessonRepository.save(wordLesson);
        return new ResponseMessage(Message.SUCCESSADDBASE);
    }

    public List<WordLesson> getWordLessonsCategory(Long categoryId) {
        return wordLessonRepository.wordLessonsCategory(categoryId);
    }

    public List<Long> wordsIdInWordLesson(Long wordLessonId) {
        WordLesson wordLesson = wordLessonRepository.findById(wordLessonId).get();
        List<Long> wordsId = new ArrayList<>();
        for (Word arr : wordLesson.getWords()) {
            wordsId.add(arr.getId());
        }
        return wordsId;
    }
}
