package com.example.learnenglish.service;

import com.example.learnenglish.model.Word;
import com.example.learnenglish.model.WordInWordLesson;
import com.example.learnenglish.model.users.User;
import com.example.learnenglish.model.WordLesson;
import com.example.learnenglish.model.users.UserWordLessonProgress;
import com.example.learnenglish.repository.CategoryRepository;
import com.example.learnenglish.repository.WordLessonRepository;
import com.example.learnenglish.repository.WordRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class WordLessonService {

    private final WordLessonRepository repository;
    private final CategoryRepository categoryRepository;
    private final WordService wordService;

    public Page<WordLesson> getWordLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Long countWordLesson() {
        return repository.lastId();
    }

    public WordLesson getWordLesson(Long id) {
        Optional<WordLesson> wordLessonOptional = repository.findById(id);
        if (wordLessonOptional.isPresent()) {
            return wordLessonOptional.get();
        }
        throw new RuntimeException("WordLesson no exist");
    }

    public WordLesson getNewWordLesson(Long id) {
        WordLesson wordLesson = new WordLesson();
        wordLesson.setName("Enter new name");
        wordLesson.setDescription("Enter description");
        wordLesson.setId(id);
        return wordLesson;
    }


    @Transactional
    public CustomResponseMessage saveWordLesson(WordLesson wordLessonDB, WordLesson wordLesson) {
        Optional.ofNullable(wordLesson.getName()).ifPresent(wordLessonDB::setName);
        Optional.ofNullable(wordLesson.getDescription()).ifPresent(wordLessonDB::setDescription);
        Optional.of(wordLesson.getSerialNumber()).ifPresent(wordLessonDB::setSerialNumber);
//        List<Word> words = wordLessonDB.getWords();
//        if (words.size() != 0 && words.size() != wordLesson.getWords().size()) {
//            List<Word> dtoWords = wordLesson.getWords();
//            Iterator<Word> iterator = words.iterator();
//            while (iterator.hasNext()) {
//                Word word = iterator.next();
//                boolean containsId = false;
//                for (Word arr : dtoWords) {
//                    if (word.getId().equals(arr.getId())) {
//                        containsId = true;
//                        break;
//                    }
//                }
//                if (!containsId) {
//                    iterator.remove();
//                    word.setWordLesson(null);
//                }
//            }
//        }
//        if (wordLessonDto.getWordsId().size() != 0) {
//            List<Word> list = wordRepository.findByIds(wordLessonDto.getWordsId());
//            for (Word arr : list) {
//                wordLessonDB.getWords().add(arr);
//                arr.setWordLesson(wordLessonDB);
//            }
//        }
//        wordLessonDB.setWords(words);
//        if (wordLessonDto.getWordLesson().getCategory() != null && wordLessonDto.getWordLesson().getCategory().getId() != 0) {
//            if (wordLessonDto.getWordLesson() == null || !wordLessonDto.getWordLesson().getCategory().getId().equals(wordLessonDB.getCategory().getId())) {
//                wordLessonDB.setCategory(wordLessonDto.getWordLesson().getCategory());
//            }
//        }
//        wordLessonRepository.save(wordLessonDB);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    @Transactional
    public CustomResponseMessage saveNewWordLesson(WordLesson wordLesson) {
        if(wordLesson.getCategory().getId() == 0)wordLesson.setCategory(null);

        List<WordInWordLesson> words = wordLesson.getWords();
        for (int i = 0; i < words.size(); i++) {
            words.get(i).setWordLesson(wordLesson);
        }
        wordLesson.setWords(words);
        repository.save(wordLesson);


//        WordLesson wordLesson = new WordLesson();
//        wordLesson.setName(wordLessonDto.getWordLesson().getName());
//        wordLesson.setDescription(wordLessonDto.getWordLesson().getDescription());
//        if (wordLessonDto.getWordsId().size() != 0) {
//            List<Word> list = wordRepository.findByIds(wordLessonDto.getWordsId());
//            for (Word arr : list) {
//                arr.setWordLesson(wordLesson);
//            }
//            wordLesson.setWords(list);
//        }
//        wordLesson.setSerialNumber(wordLessonDto.getWordLesson().getSerialNumber());
//        if (wordLessonDto.getWordLesson().getSerialNumber() == 0) {
//            wordLesson.setSerialNumber(1000);
//        }
//        wordLessonRepository.save(wordLesson);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public List<WordLesson> getWordLessonsCategory(User user, Long categoryId) {
        List<WordLesson> wordLessonList = repository.findAllByCategoryIdOrderBySerialNumber(categoryId);
        List<UserWordLessonProgress> userWordLessonProgressList = user.getWordLessonProgress();
        if (userWordLessonProgressList.size() != 0) {
            for (WordLesson wordLesson : wordLessonList) {
                for (UserWordLessonProgress arrP : userWordLessonProgressList) {
                    if (wordLesson.getId().equals(arrP.getWordLesson().getId())) {
                        wordLesson.setUserWordLessonProgress(arrP);
                    }
                }
            }
        }
        return wordLessonList;
    }

    public List<Long> wordsIdInWordLesson(Long wordLessonId) {
        WordLesson wordLesson = repository.findById(wordLessonId).get();
        List<Long> wordsId = new ArrayList<>();
//        for (Word arr : wordLesson.getWords()) {
//            wordsId.add(arr.getId());
//        }
        return wordsId;
    }


}
