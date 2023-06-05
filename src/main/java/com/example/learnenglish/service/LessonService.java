package com.example.learnenglish.service;

import com.example.learnenglish.dto.DtoTranslationPairToUI;
import com.example.learnenglish.model.Lesson;
import com.example.learnenglish.model.TranslationPair;
import com.example.learnenglish.repository.LessonRepository;
import com.example.learnenglish.repository.TranslationPairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;

    }

    public Lesson findById(long id) {
        return lessonRepository.findById(id).get();
    }

    public Page<Lesson> getLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lessonRepository.findAll(pageable);
    }

    public void lessonEdit(Lesson lesson) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lesson.getId());
        if (!lessonOptional.isPresent()) {
            lessonSave(lesson);
        } else {
            Lesson lessonFromBase = lessonOptional.get();
            lessonFromBase.setName(lesson.getName());
            lessonFromBase.setLessonInfo(lesson.getLessonInfo());
            lessonRepository.save(lessonFromBase);
        }
    }

    public void lessonSave(Lesson lesson) {
        lessonRepository.save(lesson);
    }


    public Long countLessons() {
        return lessonRepository.count();
    }
}
