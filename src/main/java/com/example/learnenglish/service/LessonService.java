package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.dto.PhraseLessonDto;
import com.example.learnenglish.model.PhraseLesson;
import com.example.learnenglish.repository.LessonRepository;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Не Буде змінюватись
@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;

    }

    public PhraseLesson getLesson(Long id) {
        Optional<PhraseLesson> phraseLessonOptional = lessonRepository.findById(id);
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
        return lessonRepository.findAll(pageable);
    }

    public CustomResponseMessage saveLesson(PhraseLessonDto phraseLessonDto) {
        Optional<PhraseLesson> lessonOptional = lessonRepository.findById(phraseLessonDto.getPhraseLesson().getId());
        if (lessonOptional.isPresent()) {
            PhraseLesson phraseLessonFromBase = lessonOptional.get();
            phraseLessonFromBase.setName(phraseLessonDto.getPhraseLesson().getName());
            phraseLessonFromBase.setDescription(phraseLessonDto.getPhraseLesson().getDescription());
            phraseLessonFromBase.setPublished(phraseLessonDto.getPhraseLesson().isPublished());
            if (phraseLessonDto.getPhraseLesson().getId() != 0 && !phraseLessonDto.getPhraseLesson().getId().equals(phraseLessonFromBase.getId())) {
                phraseLessonFromBase.setCategory(phraseLessonDto.getPhraseLesson().getCategory());
            }
            lessonRepository.save(phraseLessonFromBase);
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        } else {
            lessonSave(phraseLessonDto.getPhraseLesson());
            return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
        }
    }

    public void lessonSave(PhraseLesson phraseLesson) {
        lessonRepository.save(phraseLesson);
    }


    public Long countLessons() {
        return lessonRepository.count();
    }


}
