package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.PhraseLessonDto;
import com.example.learnenglish.model.Category;
import com.example.learnenglish.model.PhraseLesson;
import com.example.learnenglish.repository.PhraseLessonRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhraseLessonService {
    private final PhraseLessonRepository phraseLessonRepository;

    public PhraseLessonService(PhraseLessonRepository phraseLessonRepository) {
        this.phraseLessonRepository = phraseLessonRepository;

    }

    public PhraseLesson getLesson(Long id) {
        Optional<PhraseLesson> phraseLessonOptional = phraseLessonRepository.findById(id);
        if (phraseLessonOptional.isPresent()) {
            return phraseLessonOptional.get();
        } else {
            PhraseLesson phraseLesson = new PhraseLesson();
            phraseLesson.setId(id);
            phraseLesson.setName("Заняття № " + id);
            phraseLesson.setDescription("Опис заняття");
            return phraseLesson;
        }

    }

    public Page<PhraseLesson> getLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return phraseLessonRepository.findAll(pageable);
    }

    public CustomResponseMessage saveLesson(PhraseLessonDto phraseLessonDto) {
        Optional<PhraseLesson> lessonOptional = phraseLessonRepository.findById(phraseLessonDto.getPhraseLesson().getId());
        if (lessonOptional.isPresent()) {
            PhraseLesson phraseLessonFromBase = lessonOptional.get();
            phraseLessonFromBase.setName(phraseLessonDto.getPhraseLesson().getName());
            phraseLessonFromBase.setDescription(phraseLessonDto.getPhraseLesson().getDescription());
            phraseLessonFromBase.setPublished(phraseLessonDto.getPhraseLesson().isPublished());
            if (phraseLessonDto.getPhraseLesson().getCategory().getId() != 0 &&
                    !phraseLessonDto.getPhraseLesson().getCategory().getId().equals(phraseLessonFromBase.getCategory().getId())) {
                phraseLessonFromBase.setCategory(phraseLessonDto.getPhraseLesson().getCategory());
            }
            phraseLessonRepository.save(phraseLessonFromBase);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else {
            lessonSave(phraseLessonDto.getPhraseLesson());
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        }
    }

    public void lessonSave(PhraseLesson phraseLesson) {
        phraseLessonRepository.save(phraseLesson);
    }


    public Long countLessons() {
        return phraseLessonRepository.count();
    }




}
