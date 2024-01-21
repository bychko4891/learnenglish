package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.exception.ObjectNotFoundException;
import com.example.learnenglish.model.PhraseLesson;
import com.example.learnenglish.repository.PhraseLessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.Data;

import java.util.Optional;

@Service
@Data
public class PhraseLessonService {


    private final PhraseLessonRepository phraseLessonRepository;


    public PhraseLesson getPhraseLesson(long id) {
        Optional<PhraseLesson> phraseLessonOptional = phraseLessonRepository.findById(id);
        if (phraseLessonOptional.isPresent()) {
            return phraseLessonOptional.get();
        } else throw new ObjectNotFoundException("PhraseLesson with id: " + id + "not found");

    }

    public PhraseLesson getNewPhraseLesson(long id) {
            PhraseLesson phraseLesson = new PhraseLesson();
            phraseLesson.setId(id);
            phraseLesson.setName("Заняття № " + id);
            phraseLesson.setDescription("Опис заняття");
            return phraseLesson;
    }

    public Page<PhraseLesson> getLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return phraseLessonRepository.findAll(pageable);
    }

//    public CustomResponseMessage saveLesson(PhraseLesson phraseLesson) {
//        Optional<PhraseLesson> lessonOptional = phraseLessonRepository.findById(phraseLesson.getPhraseLesson().getId());
//        if (lessonOptional.isPresent()) {
//            PhraseLesson phraseLessonFromBase = lessonOptional.get();
//            phraseLessonFromBase.setName(phraseLesson.getPhraseLesson().getName());
//            phraseLessonFromBase.setDescription(phraseLesson.getPhraseLesson().getDescription());
//            phraseLessonFromBase.setPublished(phraseLesson.getPhraseLesson().isPublished());
//            if (phraseLesson.getPhraseLesson().getCategory().getId() != 0 &&
//                    !phraseLesson.getPhraseLesson().getCategory().getId().equals(phraseLessonFromBase.getCategory().getId())) {
//                phraseLessonFromBase.setCategory(phraseLesson.getPhraseLesson().getCategory());
//            }
//            phraseLessonRepository.save(phraseLessonFromBase);
//            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
//        } else {
//            lessonSave(phraseLesson.getPhraseLesson());
//            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
//        }
//        return null;
//    }

//    public void lessonSave(PhraseLesson phraseLesson) {
//        phraseLessonRepository.save(phraseLesson);
//    }


    public long lastIdPhraseLesson() {
        return phraseLessonRepository.lastId();
    }




}
